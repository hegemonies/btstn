package org.bravo.apiproxy.model

import org.bravo.model.dto.News
import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.ResultRow

data class SearchNewsResponseView(
    val message: String,
    val source: String,
    val date: Long
) {

    companion object {

        fun fromResultRow(resultRow: ResultRow) =
            SearchNewsResponseView(
                message = resultRow[NewsTable.message],
                source = resultRow[NewsTable.newsSource],
                date = resultRow[NewsTable.date]
            )

        fun fromNews(news: News) =
            SearchNewsResponseView(
                message = news.message,
                source = news.source,
                date = news.date
            )
    }
}
