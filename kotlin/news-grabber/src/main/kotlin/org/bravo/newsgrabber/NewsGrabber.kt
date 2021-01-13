package org.bravo.newsgrabber

import org.bravo.newsgrabber.initialize.database.connectToDatabase
import org.bravo.newsgrabber.initialize.database.initSchemas
import org.bravo.newsgrabber.property.app.AppProperties
import org.bravo.newsgrabber.property.database.DatabaseProperties
import org.bravo.newsgrabber.property.telegram.TelegramProperties
import org.bravo.newsgrabber.service.news.NewsService
import kotlin.system.exitProcess

suspend fun main(args: Array<String>) {
    runCatching {
        printAllProperties()
        connectToDatabase()
        initSchemas()
        // TelegramClientAuthenticate.auth() TODO: need refactoring
        NewsService.runService()
    }.getOrElse { error ->
        println("Error: $error")
        exitProcess(1)
    }
}

private fun printAllProperties() {
    println(TelegramProperties())
    println(DatabaseProperties())
    println(AppProperties())
}
