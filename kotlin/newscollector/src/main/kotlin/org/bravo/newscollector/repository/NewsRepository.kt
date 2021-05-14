package org.bravo.newscollector.repository

import org.bravo.newscollector.model.News
import org.springframework.data.jpa.repository.JpaRepository

interface NewsRepository : JpaRepository<News, Long> {

    fun existsByObjectId(objectId: Long): Boolean
}
