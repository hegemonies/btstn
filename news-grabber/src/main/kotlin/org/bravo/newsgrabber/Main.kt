package org.bravo.newsgrabber

import kotlinx.coroutines.runBlocking
import org.bravo.newsgrabber.service.auth.TelegramClientAuthenticate
import org.bravo.newsgrabber.service.news.NewsService

fun main() {
    // println(TelegramProperties().toString())
    runCatching {
        // TelegramClientAuthenticate.auth()
        runBlocking { NewsService.runService() }
    }.getOrElse { error ->
        println("Error: ${error.message}")
    }
}
