package org.bravo.newsgrabber.model.query

import org.bravo.newsgrabber.model.mapper.mapToNewsDto
import org.bravo.newsgrabber.model.table.NewsTable
import org.jetbrains.exposed.sql.select

fun objectIdExists(objectId: Long) =
    NewsTable.select { NewsTable.objectId eq objectId }
        .map { mapToNewsDto(it) }
        .firstOrNull()?.let {
            true
        } ?: false

fun objectIdNotExists(objectId: Long) =
    !objectIdExists(objectId)
