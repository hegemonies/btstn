package org.bravo.newsgrabber.model.dto

data class News(
    val message: String,
    val source: NewsSource,
    val objectId: Long,
    val date: Long
)

