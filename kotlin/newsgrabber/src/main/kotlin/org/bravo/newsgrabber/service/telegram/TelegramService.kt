package org.bravo.newsgrabber.service.telegram

import com.github.badoualy.telegram.api.TelegramClient
import com.github.badoualy.telegram.api.utils.id
import com.github.badoualy.telegram.api.utils.title
import com.github.badoualy.telegram.api.utils.toInputPeer
import com.github.badoualy.telegram.tl.api.TLMessage
import com.github.badoualy.telegram.tl.api.auth.TLAuthorization
import com.github.badoualy.telegram.tl.api.auth.TLSentCode
import com.github.badoualy.telegram.tl.exception.RpcErrorException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.bravo.newsgrabber.configuration.properties.telegram.TelegramProperties
import org.bravo.newsgrabber.filter.Filter
import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.model.NewsSource
import org.bravo.newsgrabber.service.util.getAllDialogs
import org.bravo.newsgrabber.service.util.getAllMessages
import org.bravo.newsgrabber.service.util.getLatestMessagesFromAllDialogs
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TelegramService(
    private val telegramClient: TelegramClient,
    private val telegramProperties: TelegramProperties,
    private val filter: Filter
) {

    /**
     * Request of auth code for authenticate
     *
     * @throws RpcErrorException
     * @throws IOException
     */
    fun authCodeRequest(): TLSentCode =
        telegramClient.authSendCode(
            allowFlashcall = false,
            phoneNumber = telegramProperties.phoneNumber,
            currentNumber = true
        )

    /**
     * Request for sign in
     *
     * @throws RpcErrorException
     * @throws IOException
     */
    fun signIn(codeFromMessage: String, phoneCode: String): TLAuthorization =
        telegramClient.authSignIn(
            telegramProperties.phoneNumber,
            codeFromMessage,
            phoneCode
        )

    /**
     * Read all messages from chat with special number and create [News] from it.
     * TODO: need refactoring, because [readAllNewsFromNew] has the same code
     */
    fun readLatestNewsFrom(chatNumber: Int): List<News> =
        try {
            val absDialogs = telegramClient.getAllDialogs()

            val news = mutableListOf<News>()

            absDialogs.chats.ifEmpty {
                return emptyList()
            }.firstOrNull {
                it.id == absDialogs.dialogs[chatNumber].peer.id
            }?.also { chat ->
                if (chat.title == null || !filter.sourceFilter(chat.title!!)) {
                    logger.info("Chat - ${chat.title}, dont pass filter")
                    return emptyList()
                }

                logger.info("Chat - ${chat.title}, passed the filter")

                chat.toInputPeer().let { peer ->
                    telegramClient.getLatestMessagesFromAllDialogs(peer)
                }.filter { absMessage ->
                    filter.absMessageFilter(absMessage)
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
            logger.warn("Error read all news from telegram by chat with number #$chatNumber: ${rpcError.message}")
            runBlocking {
                logger.info("delaying ${rpcError.tagInteger} sec")
                delay(rpcError.tagInteger * 1000L)
            }
            emptyList()
        } catch (commonError: Exception) {
            logger.warn("Error read all news from telegram by chat with number #$chatNumber: ${commonError.message}")
            emptyList()
        }

    /**
     * Read latest messages from chat with special number and create [News] from it.
     * TODO: need refactoring, because [readLatestNewsFrom] has the same code
     */
    fun readAllNewsFromNew(chatNumber: Int): List<News> =
        try {
            val absDialogs = telegramClient.getAllDialogs()

            val news = mutableListOf<News>()

            absDialogs.chats
                .ifEmpty {
                    return emptyList()
                }
                .firstOrNull {
                    it.id == absDialogs.dialogs[chatNumber].peer.id
                }?.also { chat ->
                    if (chat.title == null || !filter.sourceFilter(chat.title!!)) {
                        logger.info("Chat - ${chat.title}, dont pass filter")
                        return emptyList()
                    }

                    logger.info("Chat - ${chat.title}, passed the filter")

                    chat.toInputPeer().let { peer ->
                        telegramClient.getAllMessages(peer)
                    }.filter { absMessage ->
                        filter.absMessageFilter(absMessage)
                    }.map { absMessage ->
                        absMessage as TLMessage
                    }.filter { message ->
                        filter.tagFilter(message = message.message)
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
            logger.warn("Error read all news from telegram by chat with number #$chatNumber: ${rpcError.message}")
            runBlocking {
                logger.info("delaying ${rpcError.tagInteger} sec")
                delay(rpcError.tagInteger * 1000L)
            }
            readAllNewsFromNew(chatNumber)
        } catch (commonError: Exception) {
            logger.warn("Error read all news from telegram by chat with number #$chatNumber: ${commonError.message}")
            emptyList()
        }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
