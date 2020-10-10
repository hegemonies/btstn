package org.bravo.newsgrabber.model

data class News(
    val message: String,
    val source: NewsSource,
    val objectId: Long,
    val date: Long
)

