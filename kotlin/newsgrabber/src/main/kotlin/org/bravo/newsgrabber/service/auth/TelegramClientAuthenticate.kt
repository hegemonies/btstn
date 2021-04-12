package org.bravo.newsgrabber.service.auth

import org.bravo.newsgrabber.service.telegram.TelegramService
import org.slf4j.LoggerFactory
import java.util.*

object TelegramClientAuthenticate : Authenticate {

    override fun auth() {
        runCatching {
            val sentCode = TelegramService.authCodeRequest()
            println("Enter code: ")
            val code = Scanner(System.`in`).nextLine()

            val authorization = TelegramService.signIn(
                codeFromMessage = sentCode.phoneCodeHash,
                phoneCode = code
            )
            val self = authorization.user.asUser

            logger.info("You are now signed in as ${self.firstName} ${self.lastName} @ ${self.username}")
        }.getOrElse { error ->
            logger.error("Error auth: ${error.message}")
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
