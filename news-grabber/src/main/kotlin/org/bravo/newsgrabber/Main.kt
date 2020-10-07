package org.bravo.newsgrabber

import kotlinx.coroutines.runBlocking
import org.bravo.newsgrabber.initialize.database.InitSchema
import org.bravo.newsgrabber.initialize.database.databaseConnection
import org.bravo.newsgrabber.service.news.NewsService

fun main() {
    // println(TelegramProperties().toString())

    runCatching {
        databaseConnection
        InitSchema.initSchemas()
        // TelegramClientAuthenticate.auth()
        runBlocking { NewsService.runService() }
    }.getOrElse { error ->
        println("Error: ${error.message}")
    }
}
