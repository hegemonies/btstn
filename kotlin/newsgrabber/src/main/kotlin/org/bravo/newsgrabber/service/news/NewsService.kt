package org.bravo.newsgrabber.service.news

import kotlinx.coroutines.*
import org.bravo.newsgrabber.model.FetchStrategy
import org.bravo.newsgrabber.configuration.properties.app.AppProperties
import org.bravo.newsgrabber.service.grabber.TelegramGrabberService
import org.bravo.newsgrabber.strategy.telegram.TelegramFetchAllStrategy
import org.bravo.newsgrabber.strategy.telegram.TelegramFetchLatestStrategy
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

@Component
class NewsService(
    private val appProperties: AppProperties,
    private val telegramGrabberService: TelegramGrabberService,
    private val telegramFetchAllStrategy: TelegramFetchAllStrategy,
    private val telegramFetchLatestStrategy: TelegramFetchLatestStrategy
) : CommandLineRunner {

    private suspend fun runTelegramGrabber() {
        if (appProperties.strategy == FetchStrategy.ALL.strategy) {
            logger.info("Grabbing ALL news from telegram starting...")

            measureTimeMillis {
                telegramGrabberService.start(telegramFetchAllStrategy)
            }.also { elapsedTime ->
                logger.info("Grabbing ALL news from telegram took $elapsedTime ms")
            }
        }

        while (true) {
            logger.info("Grabbing LATEST news from telegram starting...")

            measureTimeMillis {
                telegramGrabberService.start(telegramFetchLatestStrategy)
            }.also { elapsedTime ->
                logger.info("Grabbing LATEST news from telegram took $elapsedTime ms")
            }

            delay(2000)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }

    override fun run(vararg args: String?) {
        runBlocking {
            val grabberServices = mutableListOf<Job>()

            grabberServices.add(
                GlobalScope.launch {
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
    }
}
