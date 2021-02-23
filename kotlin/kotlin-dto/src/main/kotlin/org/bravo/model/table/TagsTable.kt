package org.bravo.model.table

import org.jetbrains.exposed.sql.Table

object TagsTable : Table("news_grabber_tags") {
    val id = long("id").autoIncrement()
    val tag = text("tag").index()
    // val fullname = text("fullname") ???
    override val primaryKey: PrimaryKey = PrimaryKey(
        columns = arrayOf(TagsTable.id),
        name = "news_grabber_tags_pk"
    )
}
