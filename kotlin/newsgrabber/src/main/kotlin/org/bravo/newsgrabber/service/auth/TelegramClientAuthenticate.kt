package org.bravo.newsgrabber.service.auth

import org.bravo.newsgrabber.service.telegram.TelegramService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class TelegramClientAuthenticate(
    private val telegramService: TelegramService
) : Authenticate {

    override fun auth() {
        runCatching {
            val sentCode = telegramService.authCodeRequest()
            println("Enter code: ")
            val code = Scanner(System.`in`).nextLine()

            val authorization = telegramService.signIn(
                codeFromMessage = sentCode.phoneCodeHash,
                phoneCode = code
            )
            val self = authorization.user.asUser

            logger.info("You are now signed in as ${self.firstName} ${self.lastName} @ ${self.username}")
        }.getOrElse { error ->
            logger.error("Error auth: ${error.message}")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
