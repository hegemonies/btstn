package com.bravo

import com.bravo.config.connectToDatabase
import com.bravo.config.initSchemas
import com.bravo.config.telegramGrabberCoroutineScope
import com.bravo.plugins.configureRouting
import com.bravo.plugins.configureSerialization
import com.bravo.service.telegram.TelegramGrabberService.startTelegramGrabberConsumer
import io.ktor.application.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    connectToDatabase()
    runBlocking(telegramGrabberCoroutineScope.coroutineContext) {
        initSchemas()
    }

    runBlocking(telegramGrabberCoroutineScope.coroutineContext) {
        startTelegramGrabberConsumer()
    }

    configureRouting()
    configureSerialization()
}
