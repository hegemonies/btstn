package org.bravo.model.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object NewsTagsTable : IdTable<Int>("news_grabber_news_tags") {
    override val id: Column<EntityID<Int>> = integer("id").autoIncrement().entityId()
    val tagId = reference("tag_id", TagsTable.id)
    val newsId = reference("news_id", NewsTable.id)

    override val primaryKey: PrimaryKey = PrimaryKey(
        columns = arrayOf(tagId, newsId),
        name = "news_grabber_news_tags_pk"
    )
}
