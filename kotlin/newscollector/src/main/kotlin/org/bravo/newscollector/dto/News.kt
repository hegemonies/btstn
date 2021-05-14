package org.bravo.newscollector.dto

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val message: String,
    val source: String,
    val objectId: Long,
    val date: Long
)
