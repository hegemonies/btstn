package org.bravo.newsgrabber.service.grabber

import org.bravo.newsgrabber.model.News

class TelegramGrabber : NewsGrabber {

    override fun processed(): Boolean =
        saveToDb(grab())

    override fun saveToDb(news: News): Boolean {
        TODO("Not yet implemented")
    }

    override fun grab(): News {
        TODO("Not yet implemented")
    }
}
