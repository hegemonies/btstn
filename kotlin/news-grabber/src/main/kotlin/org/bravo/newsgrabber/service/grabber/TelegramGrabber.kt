package org.bravo.newsgrabber.service.grabber

import kotlinx.coroutines.delay
import org.bravo.model.dto.News
import org.bravo.newsgrabber.filter.objectIdNotExists
import org.bravo.model.table.NewsTable
import org.bravo.newsgrabber.strategy.telegram.IFetchStrategy
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

class TelegramGrabber(
    private val fetchStrategy: IFetchStrategy<Int, List<News>>
) : NewsGrabber {

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
                    logger.debug("insert news #${newsFromList.objectId}")

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
        (1..99).map { chatNumber ->
            logger.info("Read messages from chat #$chatNumber")

            fetchStrategy.fetch(chatNumber).also { news ->
                logger.debug("news from chat #$chatNumber: $news")
                delay(2000)
            }
        }.reduce { acc, list ->
            acc + list
        }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
