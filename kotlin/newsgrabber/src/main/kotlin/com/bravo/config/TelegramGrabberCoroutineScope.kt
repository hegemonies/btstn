package com.bravo.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.*

val telegramGrabberCoroutineScope =
    object : CoroutineScope {
        override val coroutineContext =
            Executors.newFixedThreadPool(4.coerceAtLeast(Runtime.getRuntime().availableProcessors()))
                .asCoroutineDispatcher()
    }
