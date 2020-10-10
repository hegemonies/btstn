package org.bravo.newsgrabber.service.grabber

import kotlinx.coroutines.delay
import org.bravo.newsgrabber.model.dto.News
import org.bravo.newsgrabber.model.query.objectIdNotExists
import org.bravo.newsgrabber.model.table.NewsTable
import org.bravo.newsgrabber.service.telegram.TelegramService
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

object TelegramGrabber : NewsGrabber {

    override suspend fun processed(): Boolean =
        saveToDb(grab())

    override suspend fun saveToDb(news: List<News>): Boolean {
        measureTimeMillis {
            transaction {
                news.filter {
                    it.message.isNotEmpty()
                }.filter {
                    objectIdNotExists(it.objectId)
                }.forEach { newsFromList ->
                    logger.info("insert news #${newsFromList.objectId}")
                    NewsTable.insert {
                        it[message] = newsFromList.message
                        it[newsSource] = newsFromList.source
                        it[objectId] = newsFromList.objectId
                        it[date] = newsFromList.date
                    }
                }
            }
        }.also { elapsedTimes ->
            logger.info("Inserting news by $elapsedTimes millis")
        }

        return true
    }

    override suspend fun grab(): List<News> =
        (1..3).map { chatNumber ->
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
