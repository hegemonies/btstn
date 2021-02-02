package org.bravo.apiproxy.model

data class SearchNewsRequest(
    val tag: String,
    val pagination: Pagination
)
