package org.bravo.newscollector.converter

import org.bravo.newscollector.utils.transformSourceToNumber
import org.bravo.newscollector.dto.News as NewsDto
import org.bravo.newscollector.model.News as NewsModel

fun NewsModel.toDto() =
    NewsDto(
        message = this.message,
        source = this.source,
        objectId = this.objectId,
        date = this.createdAt
    )

fun NewsDto.toModel() =
    NewsModel(
        message = this.message,
        source = this.source,
        objectId = this.objectId + transformSourceToNumber(this.source),
        createdAt = this.date
    )
