package org.bravo.newsgrabber.service.news

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bravo.newsgrabber.service.grabber.TelegramGrabber

object NewsService {

    private val grabbers = mutableListOf<Job>()

    suspend fun runService() {
        withContext(Dispatchers.IO) {

            grabbers.add(
                launch {
                    TelegramGrabber().processed()
                }
            )

            grabbers.joinAll()
        }
    }
}
