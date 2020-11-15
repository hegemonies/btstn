package org.bravo.newsgrabber.service.telegram

import com.github.badoualy.telegram.api.Kotlogram
import com.github.badoualy.telegram.api.TelegramApp
import com.github.badoualy.telegram.api.utils.id
import com.github.badoualy.telegram.api.utils.title
import com.github.badoualy.telegram.api.utils.toInputPeer
import com.github.badoualy.telegram.tl.api.TLMessage
import com.github.badoualy.telegram.tl.api.auth.TLAuthorization
import com.github.badoualy.telegram.tl.api.auth.TLSentCode
import com.github.badoualy.telegram.tl.exception.RpcErrorException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.bravo.model.dto.News
import org.bravo.model.dto.NewsSource
import org.bravo.newsgrabber.filter.sourceFilter
import org.bravo.newsgrabber.filter.tagFilter
import org.bravo.newsgrabber.filter.telegram.isMessage
import org.bravo.newsgrabber.property.telegram.TelegramProperties
import org.bravo.newsgrabber.repository.TelegramStorage
import org.bravo.newsgrabber.service.util.getAllDialogs
import org.bravo.newsgrabber.service.util.getAllMessages
import org.bravo.newsgrabber.service.util.getLatestMessagesFromAllDialogs
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
     * TODO: need refactoring, because [readAllNewsFromNew] has the same code
     */
    fun readLatestNewsFrom(chatNumber: Int): List<News> =
        try {
            val absDialogs = client.getAllDialogs()

            val news = mutableListOf<News>()

            absDialogs.chats.first {
                it.id == absDialogs.dialogs[chatNumber].peer.id
            }.also { chat ->
                if (chat.title == null || !sourceFilter(chat.title!!)) {
                    logger.info("Chat - ${chat.title}, dont pass filter")
                    return emptyList()
                }

                logger.info("Chat - ${chat.title}, passed the filter")

                chat.toInputPeer().let { peer ->
                    client.getLatestMessagesFromAllDialogs(peer)
                }.filter { absMessage ->
                    isMessage(absMessage)
                }.map { absMessage ->
                    absMessage as TLMessage
                }.forEach { message ->
                    news.add(
                        News(
                            message = message.message,
                            source = NewsSource.telegramSource(chat.title ?: "empty"),
                            objectId = message.id.toLong(),
                            date = message.date.toLong()
                        )
                    )
                }
            }

            news
        } catch (rpcError: RpcErrorException) {
            logger.error("Error read all news from telegram by chat with number #$chatNumber: ${rpcError.message}")
            runBlocking {
                logger.info("delaying ${rpcError.tagInteger} sec")
                delay(rpcError.tagInteger * 1000L)
            }
            emptyList()
        } catch (commonError: Exception) {
            logger.error("Error read all news from telegram by chat with number #$chatNumber: ${commonError.message}")
            emptyList()
        }

    /**
     * Read latest messages from chat with special number and create [News] from it.
     * TODO: need refactoring, because [readLatestNewsFrom] has the same code
     */
    fun readAllNewsFromNew(chatNumber: Int): List<News> =
        try {
            val absDialogs = client.getAllDialogs()

            val news = mutableListOf<News>()

            absDialogs.chats.first {
                it.id == absDialogs.dialogs[chatNumber].peer.id
            }.also { chat ->
                if (chat.title == null || !sourceFilter(chat.title!!)) {
                    logger.info("Chat - ${chat.title}, dont pass filter")
                    return emptyList()
                }

                logger.info("Chat - ${chat.title}, passed the filter")

                chat.toInputPeer().let { peer ->
                    client.getAllMessages(peer)
                }.filter { absMessage ->
                    isMessage(absMessage)
                }.map { absMessage ->
                    absMessage as TLMessage
                }.filter{ message ->
                    tagFilter(message = message.message)
                }.forEach { message ->
                    news.add(
                        News(
                            message = message.message,
                            source = NewsSource.telegramSource(chat.title ?: "empty"),
                            objectId = message.id.toLong(),
                            date = message.date.toLong()
                        )
                    )
                }
            }

            news
        } catch (rpcError: RpcErrorException) {
            logger.error("Error read all news from telegram by chat with number #$chatNumber: ${rpcError.message}")
            runBlocking {
                logger.info("delaying ${rpcError.tagInteger} sec")
                delay(rpcError.tagInteger * 1000L)
            }
            readAllNewsFromNew(chatNumber)
        } catch (commonError: Exception) {
            logger.error("Error read all news from telegram by chat with number #$chatNumber: ${commonError.message}")
            emptyList()
        }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
