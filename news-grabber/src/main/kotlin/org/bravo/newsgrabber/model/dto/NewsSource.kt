package org.bravo.newsgrabber.model.dto

object NewsSource {
    private const val telegramPrefix = "Telegram: "

    fun telegramSource(postfix: String) =
        telegramPrefix + postfix

    fun noSource() = ""
}
