package org.bravo.newsgrabber.service.grabber

import kotlinx.coroutines.delay
import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.service.telegram.TelegramService
import org.slf4j.LoggerFactory

class TelegramGrabber : NewsGrabber {

    override suspend fun processed(): Boolean =
        saveToDb(grab())

    override suspend fun saveToDb(news: List<News>): Boolean {
        // TODO("Not yet implemented")
        return true
    }

    override suspend fun grab(): List<News> {
        return (1..30).map { chatNumber ->
            logger.info("Read messages from chat #$chatNumber")
            TelegramService.readAllNewsFrom(chatNumber).also { news ->
                logger.info("news from chat #$chatNumber: $news")
                delay(2000)
            }
        }.reduce { acc, list ->
            acc + list
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
