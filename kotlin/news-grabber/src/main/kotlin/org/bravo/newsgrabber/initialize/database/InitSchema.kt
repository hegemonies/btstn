package org.bravo.newsgrabber.initialize.database

import org.bravo.newsgrabber.model.table.NewsTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initSchemas() {
    transaction {
        SchemaUtils.createMissingTablesAndColumns(NewsTable)
    }
}
