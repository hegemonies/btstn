package org.bravo.apiproxy.model

import org.bravo.model.dto.News

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
        fun fromNewsDto(dto: News) =
            SearchNewsResponseView(
                message = dto.message,
                source = dto.source,
                date = dto.date
            )
    }
}
