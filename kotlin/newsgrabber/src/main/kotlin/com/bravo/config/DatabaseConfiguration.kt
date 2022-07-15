package com.bravo.config

import io.ktor.application.*
import org.bravo.model.table.NewsTable
import org.bravo.model.table.NewsTagsTable
import org.bravo.model.table.TagsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.connectToDatabase() {

    val databaseHost: String = environment.config.property("database.host").getString()
    val databasePort: Int = environment.config.property("database.port").getString().toInt()
    val databaseName: String = environment.config.property("database.name").getString()
    val databaseUsername: String = environment.config.property("database.username").getString()
    val databasePassword: String = environment.config.property("database.password").getString()

    Database.connect(
        url = "jdbc:postgresql://$databaseHost:$databasePort/$databaseName",
        driver = "org.postgresql.Driver",
        user = databaseUsername,
        password = databasePassword
    )
}

suspend fun initSchemas() {
    newSuspendedTransaction {
        SchemaUtils.createMissingTablesAndColumns(NewsTable, TagsTable, NewsTagsTable)
    }
}
