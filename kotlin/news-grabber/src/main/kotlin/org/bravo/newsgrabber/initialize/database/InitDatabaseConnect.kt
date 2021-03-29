package org.bravo.newsgrabber.initialize.database

import org.bravo.newsgrabber.property.database.DatabaseProperties
import org.jetbrains.exposed.sql.Database

private val databaseProperties = DatabaseProperties()

fun connectToDatabase() {
    Database.connect(
        url = "jdbc:postgresql://${databaseProperties.host}:${databaseProperties.port}/${databaseProperties.databaseName}",
        driver = "org.postgresql.Driver",
        user = databaseProperties.username,
        password = databaseProperties.password
    )
}
