package org.bravo.newsgrabber.service.grabber

import org.bravo.model.dto.News
import org.bravo.newsgrabber.channel.telegramGrabberChannel
import org.bravo.newsgrabber.strategy.telegram.IFetchStrategy

class TelegramGrabberProducer(
    private val fetchStrategy: IFetchStrategy<Int, List<News>>
) {

    suspend fun grab(chatNumber: Int) {
        fetchStrategy.fetch(chatNumber).forEach { news ->
            telegramGrabberChannel.send(news)
        }
    }
}
