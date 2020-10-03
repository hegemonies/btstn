package org.bravo.newsgrabber.service.auth

import org.bravo.newsgrabber.service.telegram.TelegramService
import java.util.*

class TelegramClientAuthenticate : Authenticate {

    override fun auth() {
        runCatching {
            val sentCode = TelegramService.authCodeRequest()
            println("Enter code: ")
            val code = Scanner(System.`in`).nextLine()

            val authorization = TelegramService.signIn(
                codeFromMessage = code,
                phoneCode = sentCode.phoneCodeHash
            )
            val self = authorization.user.asUser

            println("You are now signed in as ${self.firstName} ${self.lastName} @ ${self.username}")
        }.getOrElse { error ->
            println("Error auth: ${error.message}")
        }
    }
}
