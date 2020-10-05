package org.bravo.newsgrabber.service.news

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bravo.newsgrabber.service.grabber.TelegramGrabber
import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import kotlin.system.measureTimeMillis

object NewsService {

    private val grabbers = mutableListOf<Job>()

    suspend fun runService() {
        withContext(Dispatchers.IO) {
            while (true) {
                logger.info(MarkerFactory.getMarker("news-service"), "Start grabber services...")

                val elapsedTime = measureTimeMillis {
                    grabbers.add(
                        launch {
                            TelegramGrabber().processed()
                        }
                    )

                    grabbers.joinAll()
                }

                logger.info(
                    MarkerFactory.getMarker("news-service"),
                    "Finish grabber services for $elapsedTime millis"
                )

                delay(20000)
            }
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
