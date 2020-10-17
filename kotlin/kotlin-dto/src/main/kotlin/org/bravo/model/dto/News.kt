package org.bravo.model.dto

/**
 * @see NewsSource for source field
 */
data class News(
    val message: String,
    val source: String,
    val objectId: Long,
    val date: Long
)
