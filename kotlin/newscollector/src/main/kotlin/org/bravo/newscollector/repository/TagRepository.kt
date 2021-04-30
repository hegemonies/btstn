package org.bravo.newscollector.repository

import org.bravo.newscollector.model.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long>
