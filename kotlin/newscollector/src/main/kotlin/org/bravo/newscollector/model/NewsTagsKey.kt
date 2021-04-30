package org.bravo.newscollector.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class NewsTagsKey(

    @Column(name = "tag_id")
    val tagId: Long = -1L,

    @Column(name = "news_id")
    val newsId: Long = -1L
) : Serializable