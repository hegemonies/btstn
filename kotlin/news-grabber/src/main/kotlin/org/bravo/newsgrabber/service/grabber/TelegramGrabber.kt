package org.bravo.newsgrabber.service.grabber

import kotlinx.coroutines.delay
import org.bravo.model.dto.News
import org.bravo.newsgrabber.strategy.telegram.IFetchStrategy
import org.slf4j.LoggerFactory

class TelegramGrabber(
    fetchStrategy: IFetchStrategy<Int, List<News>>
) {

    private val grabberProducer = TelegramGrabberProducer(fetchStrategy)

    suspend fun start() {
        (1..99).map { chatNumber ->
            logger.info("Read messages from chat #$chatNumber")

            grabberProducer.grab(chatNumber)
            delay(2000)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
