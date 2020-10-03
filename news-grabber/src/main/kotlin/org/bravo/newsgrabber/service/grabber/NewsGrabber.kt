package org.bravo.newsgrabber.service.grabber

import org.bravo.newsgrabber.model.News

interface NewsGrabber {
    fun processed(): Boolean
    fun saveToDb(news: News): Boolean
    fun grab(): News
}
