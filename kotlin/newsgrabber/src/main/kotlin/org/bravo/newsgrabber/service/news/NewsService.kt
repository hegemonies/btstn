package org.bravo.newsgrabber.service.news

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.bravo.newsgrabber.model.FetchStrategy
import org.bravo.newsgrabber.property.app.AppProperties
import org.bravo.newsgrabber.service.grabber.TelegramGrabber
import org.bravo.newsgrabber.service.grabber.TelegramGrabberConsumer
import org.bravo.newsgrabber.strategy.telegram.TelegramFetchAllStrategy
import org.bravo.newsgrabber.strategy.telegram.TelegramFetchLatestStrategy
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

object NewsService {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val appProperties = AppProperties()

    suspend fun runService() {
        val grabberServices = mutableListOf<Job>()

        grabberServices.add(
            GlobalScope.launch {
                TelegramGrabberConsumer.start()
                runCatching {
                    runTelegramGrabber()
                }.getOrElse { error ->
                    logger.error("Error while telegram grabbing: ${error.message}")
                    exitProcess(1)
                }
            }
        )

        grabberServices.joinAll()
    }

    private suspend fun runTelegramGrabber() {
        if (appProperties.strategy == FetchStrategy.ALL) {
            logger.info("Grabbing ALL news from telegram starting...")

            measureTimeMillis {
                TelegramGrabber(TelegramFetchAllStrategy).start()
            }.also { elapsedTime ->
                logger.info("Grabbing ALL news from telegram took $elapsedTime ms")
            }
        }

        val grabberInstance = TelegramGrabber(TelegramFetchLatestStrategy)

        while (true) {
            logger.info("Grabbing LATEST news from telegram starting...")

            measureTimeMillis {
                grabberInstance.start()
            }.also { elapsedTime ->
                logger.info("Grabbing LATEST news from telegram took $elapsedTime ms")
            }

            delay(20000)
        }
    }
}
