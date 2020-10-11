package org.bravo.newsgrabber.service.news

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import org.bravo.newsgrabber.model.FetchStrategy
import org.bravo.newsgrabber.property.app.AppProperties
import org.bravo.newsgrabber.service.grabber.TelegramGrabber
import org.bravo.newsgrabber.strategy.telegram.TelegramFetchAllStrategy
import org.bravo.newsgrabber.strategy.telegram.TelegramFetchLatestStrategy
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

object NewsService {

    private val grabbers = mutableListOf<Job>()
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val appProperties = AppProperties()

    suspend fun runService() {
        withContext(Dispatchers.IO) {
            while (true) {
                logger.info("Start grabber services...")

                val elapsedTime = measureTimeMillis {
                    grabbers.add(
                        async {
                            when (appProperties.strategy) {
                                FetchStrategy.ALL -> TelegramGrabber(TelegramFetchAllStrategy).processed()
                                FetchStrategy.LATEST -> TelegramGrabber(TelegramFetchLatestStrategy).processed()
                            }
                        }
                    )

                    grabbers.joinAll()
                }

                logger.info("Finish grabber services for $elapsedTime millis")

                delay(20000)
            }
        }
    }
}
