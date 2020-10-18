package org.bravo.newsgrabber.initialize.database

import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun initSchemas() {
    transaction {
        SchemaUtils.createMissingTablesAndColumns(NewsTable)
    }
}
