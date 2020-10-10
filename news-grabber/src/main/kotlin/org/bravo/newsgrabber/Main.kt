package org.bravo.newsgrabber

import kotlinx.coroutines.runBlocking
import org.bravo.newsgrabber.initialize.database.connectToDatabase
import org.bravo.newsgrabber.initialize.database.initSchemas
import org.bravo.newsgrabber.service.news.NewsService

fun main() {
    // println(TelegramProperties().toString())

    runCatching {
        connectToDatabase()
        initSchemas()
        // TelegramClientAuthenticate.auth()
        runBlocking {
            NewsService.runService()
        }
    }.getOrElse { error ->
        println("Error: ${error.message}")
    }
}
