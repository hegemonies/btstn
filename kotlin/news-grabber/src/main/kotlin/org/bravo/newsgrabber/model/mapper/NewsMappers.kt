package org.bravo.newsgrabber.model.mapper

import org.bravo.model.dto.News
import org.bravo.newsgrabber.model.table.NewsTable
import org.jetbrains.exposed.sql.ResultRow

fun mapToNewsDto(resultRow: ResultRow) =
    News(
        message = resultRow[NewsTable.message],
        source = resultRow[NewsTable.newsSource],
        objectId = resultRow[NewsTable.objectId],
        date = resultRow[NewsTable.date]
    )
