package com.bravo.service.telegram

import com.bravo.channel.telegramGrabberChannel
import com.bravo.config.telegramGrabberCoroutineScope
import com.bravo.filter.objectIdExists
import io.ktor.application.*
import kotlinx.coroutines.launch
import org.bravo.model.dto.News
import org.bravo.model.dto.Tag
import org.bravo.model.table.NewsTable
import org.bravo.model.table.NewsTagsTable
import org.bravo.model.table.TagsTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis


object TelegramGrabberService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun filterNews(news: News): Boolean {
        if (news.message.isEmpty()) {
            return false
        }

        val objectId = news.objectId + transformSourceToNumber(news.source)

        if (objectIdExists(objectId)) {
            return false
        }

        return true
    }

    private suspend fun saveToDatabase(newsDto: News) {
        newSuspendedTransaction {
            runCatching {
                // save to database
                logger.debug("insert news #${newsDto.objectId}")

                // filter
                if (!filterNews(newsDto)) {
                    return@newSuspendedTransaction
                }

                val newObjectId = newsDto.objectId + transformSourceToNumber(newsDto.source)

                val news = runCatching {
                    NewsTable.insert {
                        it[message] = newsDto.message
                        it[newsSource] = newsDto.source
                        it[objectId] = newObjectId
                        it[date] = newsDto.date
                    }.resultedValues?.first().let {
                        News.fromResultRow(it!!)
                    }
                }.getOrElse { error ->
                    logger.error("Can not insert news: ${error.message}")
                    return@newSuspendedTransaction
                }

                val tags = findTags(news.message).also {
                    logger.debug("find tags: $it")
                }

                tags.forEach { tag ->
                    val tagDto = TagsTable.select { TagsTable.tag eq tag.toLowerCase() }
                        .map { row ->
                            Tag.fromResultRow(row)
                        }.firstOrNull()
                        ?: run {
                            TagsTable.insert {
                                it[TagsTable.tag] = tag.toLowerCase()
                            }

                            TagsTable.select { TagsTable.tag eq tag.toLowerCase() }
                                .map { row ->
                                    Tag.fromResultRow(row)
                                }.firstOrNull()
                                ?: run {
                                    logger.error("Can not find tag #$tag")
                                    return@forEach
                                }
                        }

                    NewsTagsTable.insert {
                        it[tagId] = tagDto.id
                        it[newsId] = news.id
                    }
                }
            }.getOrElse { error ->
                logger.error("Can not save news to database: ${error.message}")
                rollback()
            }
        }
    }

    suspend fun Application.startTelegramGrabberConsumer() {
        telegramGrabberCoroutineScope.launch {
            infinityConsume()
        }
    }

    private suspend fun infinityConsume() {
        logger.info("TelegramGrabberConsumer is started")

        while (true) {
            val news = telegramGrabberChannel.receive()

//            logger.debug("Received $news")

            measureTimeMillis {
                saveToDatabase(news)
            }.also { elapsedTime ->
                logger.debug("Insert news took: $elapsedTime ms")
            }
        }
    }
}
