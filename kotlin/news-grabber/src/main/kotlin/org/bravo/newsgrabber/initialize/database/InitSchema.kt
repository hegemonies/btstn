package org.bravo.newsgrabber.initialize.database

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bravo.model.dto.News
import org.bravo.model.table.NewsTable
import org.bravo.model.table.NewsTagsTable
import org.bravo.model.table.TagsTable
import org.bravo.newsgrabber.channel.telegramGrabberChannel
import org.bravo.newsgrabber.service.util.transformSourceToNumber
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory

suspend fun initSchemas() {
    newSuspendedTransaction {
        SchemaUtils.createMissingTablesAndColumns(NewsTable, TagsTable, NewsTagsTable)
        GlobalScope.launch { migrateOfTagIndexing() }
    }
}

// process all news message and add tags
suspend fun migrateOfTagIndexing() {

    val logger = LoggerFactory.getLogger("migrateOfTagIndexing")

    logger.info("Start migrate of tag indexing")

    newSuspendedTransaction {
        NewsTable.selectAll()
            .map { row ->
                val news = News.fromResultRow(row)

                news.copy(
                    objectId = news.objectId - transformSourceToNumber(news.source)
                )
            }.forEach { news ->
                telegramGrabberChannel.send(news)
            }
    }

    logger.info("Finish migrate of tag indexing")
}
