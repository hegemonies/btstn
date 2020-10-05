package org.bravo.newsgrabber.service.grabber

import org.bravo.newsgrabber.model.News

interface NewsGrabber {
    suspend fun processed(): Boolean
    suspend fun saveToDb(news: List<News>): Boolean
    suspend fun grab(): List<News>
}
