package org.bravo.newsgrabber.model.mapper

import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.model.NewsTable
import org.jetbrains.exposed.sql.ResultRow

fun mapToNewsDto(resultRow: ResultRow) =
    News(
        message = resultRow[NewsTable.message],
        source = resultRow[NewsTable.newsSource],
        objectId = resultRow[NewsTable.objectId],
        date = resultRow[NewsTable.date]
    )
