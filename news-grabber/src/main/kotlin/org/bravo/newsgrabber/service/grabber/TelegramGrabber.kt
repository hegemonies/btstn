package org.bravo.newsgrabber.service.grabber

import kotlinx.coroutines.delay
import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.model.NewsTable
import org.bravo.newsgrabber.model.mapper.mapToNewsDto
import org.bravo.newsgrabber.service.telegram.TelegramService
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object TelegramGrabber : NewsGrabber {

    override suspend fun processed(): Boolean =
        saveToDb(grab())

    override suspend fun saveToDb(news: List<News>): Boolean {
        transaction {
            news.forEach { newsFromList ->
                if (newsFromList.message.isNotEmpty()) {
                    NewsTable.select { NewsTable.objectId eq newsFromList.objectId }
                        .map { mapToNewsDto(it) }
                        .firstOrNull()
                        ?: NewsTable.insert {
                            it[message] = newsFromList.message
                            it[newsSource] = newsFromList.source
                            it[objectId] = newsFromList.objectId
                            it[date] = newsFromList.date
                        }
                }
            }
            commit()
        }

        return true
    }

    override suspend fun grab(): List<News> =
        (1..50).map { chatNumber ->
            logger.info("Read messages from chat #$chatNumber")

            TelegramService.readAllNewsFrom(chatNumber).also { news ->
                logger.debug("news from chat #$chatNumber: $news")
                delay(4000)
            }
        }.reduce { acc, list ->
            acc + list
        }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
