package org.bravo.newsgrabber.service.grabber

import kotlinx.coroutines.delay
import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.configuration.properties.telegram.TelegramProperties
import org.bravo.newsgrabber.strategy.telegram.IFetchStrategy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TelegramGrabberService(
    private val telegramGrabberProducer: TelegramGrabberProducer,
    private val telegramProperties: TelegramProperties,
) {

    suspend fun start(fetchStrategy: IFetchStrategy<Int, List<News>>) {
        (1..telegramProperties.maxChannelsCount).map { chatNumber ->
            logger.info("Read messages from chat #$chatNumber")

            telegramGrabberProducer.grab(fetchStrategy, chatNumber)
            delay(2000)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
