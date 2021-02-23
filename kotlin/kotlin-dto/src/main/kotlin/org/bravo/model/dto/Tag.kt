package org.bravo.model.dto

import org.bravo.model.table.TagsTable
import org.jetbrains.exposed.sql.ResultRow

data class Tag(
    val id: Long,
    val tag: String
) {
    companion object {
        fun fromResultRow(resultRow: ResultRow) =
            Tag(
                id = resultRow[TagsTable.id],
                tag = resultRow[TagsTable.tag]
            )
    }
}
