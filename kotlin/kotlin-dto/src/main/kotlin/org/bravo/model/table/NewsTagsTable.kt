package org.bravo.model.table

import org.jetbrains.exposed.dao.id.IntIdTable

object NewsTagsTable : IntIdTable("news_grabber_news_tags") {
    val tagId = reference("tag_id", TagsTable.id)
    val newsId = reference("news_id", NewsTable.id)
    override val primaryKey: PrimaryKey = PrimaryKey(
        columns = arrayOf(tagId, newsId),
        name = "news_grabber_news_tags_pk"
    )
}
