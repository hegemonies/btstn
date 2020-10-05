package org.bravo.newsgrabber.model

data class News(
    val message: String,
    val from: NewsSource,
    val objectId: Int
)
