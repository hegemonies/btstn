package org.bravo.newsgrabber.model.table

import org.bravo.newsgrabber.model.dto.NewsSource
import org.jetbrains.exposed.sql.Table

object NewsTable : Table("news_grabber_news") {
    val id = long("id").autoIncrement()
    val message = text("message").default("")
    val newsSource = enumeration("source", NewsSource::class).default(NewsSource.NO)
    val objectId = long("object_id").default(0)
    val date = long("date").default(0)
    override val primaryKey: PrimaryKey = PrimaryKey(
        columns = arrayOf(id),
        name = "news_grabber_news_pk"
    )
}
