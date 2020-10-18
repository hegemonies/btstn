package org.bravo.apiproxy.property

import io.ktor.application.*
import io.ktor.util.*

@KtorExperimentalAPI
val Application.databaseHost get() = environment.config.property("database.host").getString()

@KtorExperimentalAPI
val Application.databasePort get() = environment.config.property("database.port").getString()

@KtorExperimentalAPI
val Application.databaseUsername get() = environment.config.property("database.username").getString()

@KtorExperimentalAPI
val Application.databasePassword get() = environment.config.property("database.password").getString()
