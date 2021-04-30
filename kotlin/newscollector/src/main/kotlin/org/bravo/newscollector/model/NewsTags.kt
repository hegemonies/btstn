package org.bravo.newscollector.model

import javax.persistence.*

@Entity
class NewsTags(

    @EmbeddedId
    val id: NewsTagsKey,

    @ManyToOne
    @MapsId("newsId")
    @JoinColumn(name = "news_id")
    val news: News,

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    val tag: Tag
)