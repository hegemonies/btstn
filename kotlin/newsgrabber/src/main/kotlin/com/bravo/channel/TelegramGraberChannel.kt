package com.bravo.channel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import org.bravo.model.dto.News

val telegramGrabberChannel = Channel<News>(capacity = UNLIMITED)
