package org.bravo.newsgrabber.channel

import kotlinx.coroutines.channels.Channel
import org.bravo.model.dto.News

val telegramGrabberChannel = Channel<News>()
