package org.bravo.newsgrabber.service.telegram

import com.github.badoualy.telegram.api.Kotlogram
import com.github.badoualy.telegram.api.TelegramApp
import com.github.badoualy.telegram.tl.api.auth.TLAuthorization
import com.github.badoualy.telegram.tl.api.auth.TLSentCode
import org.bravo.newsgrabber.property.telegram.TelegramProperties
import org.bravo.newsgrabber.repository.TelegramStorage

object TelegramService {

    private val properties = TelegramProperties()

    private val application = with(properties) {
        TelegramApp(
            apiId = apiId,
            apiHash = apiHash,
            appVersion = appVersion,
            deviceModel = clientDeviceModel,
            langCode = languageCode,
            systemVersion = clientDeviceSystemVersion
        )
    }

    private val storage = TelegramStorage()

    private val client = Kotlogram.getDefaultClient(
        application = application,
        apiStorage = storage
    )

    /**
     * Request of auth code for authenticate
     *
     * @throws RpcErrorException
     * @throws IOException
     */
    fun authCodeRequest(): TLSentCode =
        client.authSendCode(
            allowFlashcall = false,
            phoneNumber = properties.phoneNumber,
            currentNumber = true
        )

    /**
     * Request for sign in
     *
     * @throws RpcErrorException
     * @throws IOException
     */
    fun signIn(codeFromMessage: String, phoneCode: String): TLAuthorization =
        client.authSignIn(
            properties.phoneNumber,
            codeFromMessage,
            phoneCode
        )
}
