package org.bravo.apiproxy.model

import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.ResultRow

data class SearchNewsRequest(
    val tag: String
)

data class SearchNewsResponse(
    val view: List<SearchNewsResponseView>
)

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
    }
}
