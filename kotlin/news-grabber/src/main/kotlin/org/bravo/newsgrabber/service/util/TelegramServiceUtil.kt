package org.bravo.newsgrabber.service.util

import com.github.badoualy.telegram.api.TelegramClient
import com.github.badoualy.telegram.tl.api.TLAbsInputPeer
import com.github.badoualy.telegram.tl.api.TLAbsMessage
import com.github.badoualy.telegram.tl.api.TLInputPeerEmpty
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs
import com.github.badoualy.telegram.tl.api.messages.TLAbsMessages
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun TelegramClient.getAllDialogs(): TLAbsDialogs =
    this.messagesGetDialogs(
        false,
        0,
        0,
        TLInputPeerEmpty(),
        Int.MAX_VALUE
    )

fun TelegramClient.getMessages(peer: TLAbsInputPeer?, limit: Int): TLAbsMessages =
    this.messagesGetHistory(
        peer,
        0,
        0,
        0,
        limit,
        0,
        0
    )

fun TelegramClient.getMessages(peer: TLAbsInputPeer?, limit: Int, offset: Int): TLAbsMessages =
    this.messagesGetHistory(
        peer,
        0,
        0,
        offset,
        limit,
        0,
        0
    )

fun TelegramClient.getAllMessages(peer: TLAbsInputPeer?): List<TLAbsMessage> {
    val limit = 100
    var offset = 0

    val messagesList = mutableListOf<TLAbsMessage>()

    while (offset <= 5000) {
        val messages = this.getMessages(peer, limit, offset)

        if (messages.messages.count() <= 0) {
            break
        }

        messagesList.addAll(messages.messages)
        offset += messages.messages.count()

        runBlocking {
            delay(1000)
        }
    }

    return messagesList
}

/**
 * Getting latest 100 messages from char (by peer)
 */
fun TelegramClient.getLatestMessagesFromAllDialogs(peer: TLAbsInputPeer?): List<TLAbsMessage> =
    this.getMessages(peer, limit = 100, offset = 0).messages

fun transformSourceToNumber(source: String): Int {
    val hash = source.hashCode()

    if (hash < 0) {
        return hash * -1
    }

    return hash
}
