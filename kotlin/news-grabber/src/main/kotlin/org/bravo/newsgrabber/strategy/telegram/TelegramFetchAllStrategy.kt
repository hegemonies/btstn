package org.bravo.newsgrabber.strategy.telegram

import org.bravo.model.dto.News
import org.bravo.newsgrabber.service.telegram.TelegramService

object TelegramFetchAllStrategy : IFetchStrategy<Int, List<News>> {

    override fun fetch(from: Int): List<News> =
        TelegramService.readAllNewsFromNew(from)
}
