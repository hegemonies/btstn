package org.bravo.newsgrabber.strategy.telegram

import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.service.telegram.TelegramService
import org.springframework.stereotype.Service

@Service
class TelegramFetchAllStrategy(
    private val telegramService: TelegramService
) : IFetchStrategy<Int, List<News>> {

    override fun fetch(from: Int): List<News> =
        telegramService.readAllNewsFromNew(from)
}
