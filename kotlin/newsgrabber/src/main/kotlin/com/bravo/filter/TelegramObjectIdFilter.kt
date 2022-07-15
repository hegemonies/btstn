package com.bravo.filter

import org.bravo.model.mapper.mapToNewsDto
import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.select

fun objectIdExists(objectId: Long) =
    NewsTable.select { NewsTable.objectId eq objectId }
        .map { mapToNewsDto(it) }
        .firstOrNull()?.let {
            true
        } ?: false
