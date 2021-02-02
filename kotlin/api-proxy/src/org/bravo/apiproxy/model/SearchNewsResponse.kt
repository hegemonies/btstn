package org.bravo.apiproxy.model

data class SearchNewsResponse(
    val view: List<SearchNewsResponseView>,
    val total: Long
) : Response
