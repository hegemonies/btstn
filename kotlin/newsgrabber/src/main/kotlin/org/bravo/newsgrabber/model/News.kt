package org.bravo.newsgrabber.model

data class News(
    val id: Long = 0,
    val message: String,
    val source: String,
    val objectId: Long,
    val date: Long
)
