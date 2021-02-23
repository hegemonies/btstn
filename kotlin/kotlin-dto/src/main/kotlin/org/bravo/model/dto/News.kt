package org.bravo.model.dto

import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.ResultRow

/**
 * @see NewsSource for source field
 */
data class News(
    val id: Long = 0,
    val message: String,
    val source: String,
    val objectId: Long,
    val date: Long
) {
    companion object {
        fun fromResultRow(resultRow: ResultRow) =
            News(
                id = resultRow[NewsTable.id],
                message = resultRow[NewsTable.message],
                source = resultRow[NewsTable.newsSource],
                objectId = resultRow[NewsTable.objectId],
                date = resultRow[NewsTable.date]
            )
    }
}
