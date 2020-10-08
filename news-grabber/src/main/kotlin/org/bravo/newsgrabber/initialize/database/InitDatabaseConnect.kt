package org.bravo.newsgrabber.initialize.database

import org.jetbrains.exposed.sql.Database

val databaseConnection = Database.connect(
    url = "jdbc:postgresql://localhost:5432/bravo_news",
    driver = "org.postgresql.Driver",
    user = "postgres",
    password = "postgres"
)