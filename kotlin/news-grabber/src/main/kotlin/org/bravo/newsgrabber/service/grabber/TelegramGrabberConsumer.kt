package org.bravo.newsgrabber.service.grabber

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bravo.model.dto.News
import org.bravo.model.table.NewsTable
import org.bravo.newsgrabber.channel.telegramGrabberChannel
import org.bravo.newsgrabber.filter.objectIdExists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransaction
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

object TelegramGrabberConsumer {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun filterNews(news: News): Boolean {
        if (news.message.isEmpty()) {
            return false
        }

        if (objectIdExists(news.objectId)) {
            return false
        }

        return true
    }

    private suspend fun saveToDatabase(news: News) {
        @Suppress("DeferredResultUnused")
        newSuspendedTransaction {
            suspendedTransaction {
                // filter
                if (!filterNews(news)) {
                    return@suspendedTransaction
                }

                // save to database
                logger.debug("insert news #${news.objectId}")

                NewsTable.insert {
                    it[message] = news.message
                    it[newsSource] = news.source
                    it[objectId] = news.objectId
                    it[date] = news.date
                }
            }
        }
    }

    suspend fun start() {
        GlobalScope.launch {
            infinityConsume()
        }
    }

    private suspend fun infinityConsume() {
        logger.info("TelegramGrabberConsumer is started")

        while (true) {
            val news = telegramGrabberChannel.receive()

            measureTimeMillis {
                saveToDatabase(news)
            }.also { elapsedTime ->
                logger.debug("Insert news took: $elapsedTime ms")
            }
        }
    }
}
