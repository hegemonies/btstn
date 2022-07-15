package org.bravo.apiproxy.property

import io.ktor.application.*

val Application.databaseHost get() = environment.config.property("database.host").getString()

val Application.databasePort get() = environment.config.property("database.port").getString()

val Application.databaseUsername get() = environment.config.property("database.username").getString()

val Application.databasePassword get() = environment.config.property("database.password").getString()
