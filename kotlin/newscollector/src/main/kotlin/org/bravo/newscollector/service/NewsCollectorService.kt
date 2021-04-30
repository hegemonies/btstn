package org.bravo.newscollector.service

import org.bravo.newscollector.model.News
import org.bravo.newscollector.model.NewsTags
import org.bravo.newscollector.model.NewsTagsKey
import org.bravo.newscollector.model.Tag
import org.bravo.newscollector.repository.NewsRepository
import org.bravo.newscollector.repository.NewsTagsRepository
import org.bravo.newscollector.repository.TagRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class NewsCollectorService(
    private val newsRepository: NewsRepository,
    private val tagRepository: TagRepository,
    private val newsTagsRepository: NewsTagsRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun foo() {
        val tag = tagRepository.save(
            Tag(
                tag = "AAPL"
            )
        )

        val news = newsRepository.save(
            News(
                message = "news message",
                source = "telegram",
                objectId = 1,
                createdAt = 1
            )
        )

        newsTagsRepository.save(
            NewsTags(
                id = NewsTagsKey(
                    tagId = tag.id,
                    newsId = news.id
                ),
                tag = tag,
                news = news
            )
        )

        logger.info("$tag")
        logger.info("$news")
        logger.info("${newsRepository.findAll()}")
        logger.info("${tagRepository.findAll()}")
        logger.info("${newsTagsRepository.findAll()}")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}


//object TelegramGrabberConsumer {
//    private val logger = LoggerFactory.getLogger(this::class.java)
//
//    private fun filterNews(news: News): Boolean {
//        if (news.message.isEmpty()) {
//            return false
//        }
//
//        if (objectIdExists(news.objectId)) {
//            return false
//        }
//
//        return true
//    }
//
//    private suspend fun saveToDatabase(newsDto: News) {
//        newSuspendedTransaction {
//            runCatching {
//                // save to database
//                logger.debug("insert news #${newsDto.objectId}")
//
//                // filter
//                if (!filterNews(newsDto)) {
//                    return@newSuspendedTransaction
//                }
//
//                val news = runCatching {
//                    NewsTable.insert {
//                        it[message] = newsDto.message
//                        it[newsSource] = newsDto.source
//                        it[objectId] = newsDto.objectId
//                        it[date] = newsDto.date
//                    }.resultedValues?.first().let {
//                        News.fromResultRow(it!!)
//                    }
//                }.getOrElse { error ->
//                    logger.error("Can not insert news: ${error.message}")
//                    return@newSuspendedTransaction
//                }
//
//                val tags = findTags(news.message).also {
//                    logger.debug("find tags: $it")
//                }
//
//                tags.forEach { tag ->
//                    val tagDto = TagsTable.select { TagsTable.tag eq tag.toLowerCase() }
//                        .map { row ->
//                            Tag.fromResultRow(row)
//                        }.firstOrNull()
//                        ?: run {
//                            TagsTable.insert {
//                                it[TagsTable.tag] = tag.toLowerCase()
//                            }
//
//                            TagsTable.select { TagsTable.tag eq tag.toLowerCase() }
//                                .map { row ->
//                                    Tag.fromResultRow(row)
//                                }.firstOrNull()
//                                ?: run {
//                                    logger.error("Can not find tag #$tag")
//                                    return@forEach
//                                }
//                        }
//
//                    NewsTagsTable.insert {
//                        it[tagId] = tagDto.id
//                        it[newsId] = news.id
//                    }
//                }
//            }.getOrElse { error ->
//                logger.error("Can not save news to database: ${error.message}")
//                rollback()
//            }
//        }
//    }
//
//    suspend fun start() {
//        GlobalScope.launch {
//            infinityConsume()
//        }
//    }
//
//    private suspend fun infinityConsume() {
//        logger.info("TelegramGrabberConsumer is started")
//
//        while (true) {
//            val news = telegramGrabberChannel.receive()
//
//            measureTimeMillis {
//                saveToDatabase(news)
//            }.also { elapsedTime ->
//                logger.debug("Insert news took: $elapsedTime ms")
//            }
//        }
//    }
//}
