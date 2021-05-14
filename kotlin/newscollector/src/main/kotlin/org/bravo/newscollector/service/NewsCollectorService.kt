package org.bravo.newscollector.service

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bravo.newscollector.converter.toModel
import org.bravo.newscollector.dto.News
import org.bravo.newscollector.model.NewsTags
import org.bravo.newscollector.model.NewsTagsKey
import org.bravo.newscollector.model.Tag
import org.bravo.newscollector.repository.NewsRepository
import org.bravo.newscollector.repository.NewsTagsRepository
import org.bravo.newscollector.repository.TagRepository
import org.bravo.newscollector.utils.findTags
import org.bravo.newscollector.utils.transformSourceToNumber
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import java.nio.charset.Charset

@Service
class NewsCollectorService(
    private val newsRepository: NewsRepository,
    private val tagRepository: TagRepository,
    private val newsTagsRepository: NewsTagsRepository
) : MessageListener {

    private fun filterNews(message: String, objectId: Long, source: String): Boolean {
        if (message.isEmpty()) {
            return false
        }

        if (newsRepository.existsByObjectId(objectId + transformSourceToNumber(source))) {
            return false
        }

        return true
    }

    override fun onMessage(message: Message) {
        val news = decodeMessage(message.body).getOrElse { error ->
            logger.warn("Can not decode message to News dto: ${error.message}")
            return
        }.let { dto ->
            dto.copy(
                message = transformNewsMessage(dto.message)
            )
        }

        saveToDatabase(news)
    }

    private fun decodeMessage(message: ByteArray): Result<News> =
        runCatching {
            Json.decodeFromString(message.toString(Charset.forName("UTF-8")))
        }

    private fun transformNewsMessage(message: String): String =
        message.replace("[new-line]", "\n")

    @Transactional
    fun saveToDatabase(news: News) {
        if (!filterNews(news.message, news.objectId, news.source)) {
            return
        }

        val newsModel = runCatching {
            newsRepository.save(news.toModel())
        }.getOrElse { error ->
            logger.warn("Can not save news to database: ${error.message}")
            return
        }

        val tags = findTags(news.message)

        val tagModels = saveTags(tags).getOrElse { error ->
            logger.warn("Can not save tags to database: ${error.message}")
            rollbackTransaction()
            return
        }

        tagModels.forEach { tagModel ->
            runCatching {
                newsTagsRepository.save(
                    NewsTags(
                        id = NewsTagsKey(
                            tagId = tagModel.id,
                            newsId = newsModel.id
                        ),
                        news = newsModel,
                        tag = tagModel
                    )
                )
            }.getOrElse { error ->
                logger.warn("Can not save news tags refs to database: ${error.message}")
                rollbackTransaction()
                return
            }
        }
    }

    /**
     * Находит или создает новые модели тегов в базе данных.
     */
    private fun saveTags(tags: List<String>): Result<List<Tag>> =
        runCatching {
            tags.map { tag ->
                tagRepository.findByTag(tag)
                    ?: tagRepository.save(Tag(tag = tag))
            }
        }

    private fun rollbackTransaction() = TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
