package org.bravo.newscollector.repository

import org.bravo.newscollector.model.NewsTags
import org.bravo.newscollector.model.NewsTagsKey
import org.springframework.data.jpa.repository.JpaRepository

interface NewsTagsRepository : JpaRepository<NewsTags, NewsTagsKey>
