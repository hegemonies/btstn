package org.bravo.newsgrabber.service.telegram

import com.github.badoualy.telegram.api.Kotlogram
import com.github.badoualy.telegram.api.TelegramApp
import com.github.badoualy.telegram.api.utils.id
import com.github.badoualy.telegram.api.utils.toInputPeer
import com.github.badoualy.telegram.tl.api.TLAbsChat
import com.github.badoualy.telegram.tl.api.TLInputPeerEmpty
import com.github.badoualy.telegram.tl.api.TLMessage
import com.github.badoualy.telegram.tl.api.TLPeerUser
import com.github.badoualy.telegram.tl.api.TLUser
import com.github.badoualy.telegram.tl.api.auth.TLAuthorization
import com.github.badoualy.telegram.tl.api.auth.TLSentCode
import com.github.badoualy.telegram.tl.core.TLObject
import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.model.NewsSource
import org.bravo.newsgrabber.property.telegram.TelegramProperties
import org.bravo.newsgrabber.repository.TelegramStorage
import org.slf4j.LoggerFactory

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

    /**
     * Read all messages from chat with special number and create [News] from it.
     * TODO: need will refactor
     */
    fun readAllNewsFrom(chatNumber: Int): List<News> =
        runCatching {
            val tlAbsDialogs = client.messagesGetDialogs(
                false,
                0,
                0,
                TLInputPeerEmpty(),
                Int.MAX_VALUE
            )

            val tlAbsPeer = tlAbsDialogs.dialogs[chatNumber].peer
            val tlPeerObj: TLObject =
                if (tlAbsPeer is TLPeerUser) tlAbsDialogs.users.first { it.id == tlAbsPeer.id }
                else tlAbsDialogs.chats.first { it.id == tlAbsPeer.id }

            // Retrieve inputPeer to get message history
            val inputPeer = when (tlPeerObj) {
                is TLUser -> tlPeerObj.toInputPeer()
                is TLAbsChat -> tlPeerObj.toInputPeer()
                else -> null
            } ?: TLInputPeerEmpty()

            val tlAbsMessages = client.messagesGetHistory(
                inputPeer,
                0,
                0,
                0,
                Int.MAX_VALUE,
                0,
                0
            )

            val news = mutableListOf<News>()

            tlAbsMessages.messages.reversed().forEach {
                val messageContent =
                    when (it) {
                        is TLMessage -> it.message
                        else -> ""
                    }

                news.add(
                    News(
                        message = messageContent,
                        source = NewsSource.Telegram,
                        objectId = it.id.toLong()
                    )
                )
            }

            news
        }.getOrElse { error ->
            logger.error("Error read all news from telegram by chat with number #$chatNumber: ${error.message}")
            emptyList<News>()
        }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
