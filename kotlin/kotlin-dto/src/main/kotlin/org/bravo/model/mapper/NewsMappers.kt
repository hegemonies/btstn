package org.bravo.model.mapper

import org.bravo.model.dto.News
import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.ResultRow

fun mapToNewsDto(resultRow: ResultRow) =
    News(
        id = resultRow[NewsTable.id],
        message = resultRow[NewsTable.message],
        source = resultRow[NewsTable.newsSource],
        objectId = resultRow[NewsTable.objectId],
        date = resultRow[NewsTable.date]
    )
