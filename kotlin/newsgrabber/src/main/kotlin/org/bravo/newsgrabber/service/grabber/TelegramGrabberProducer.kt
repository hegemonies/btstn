package org.bravo.newsgrabber.service.grabber

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.strategy.telegram.IFetchStrategy
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class TelegramGrabberProducer(
    private val rabbitTemplate: RabbitTemplate,
    private val queue: Queue
) {

    suspend fun grab(fetchStrategy: IFetchStrategy<Int, List<News>>, chatNumber: Int) {
        val newsList = fetchStrategy.fetch(chatNumber)

        GlobalScope.launch(Dispatchers.IO) {
            newsList.forEach { news ->
                val json = runCatching {
                    Json.encodeToString(
                        news.copy(
                            message = news.message.replace("\n", "[new-line]")
                        )
                    )
                }.getOrElse { error ->
                    logger.error("Can not send news to rabbitmq: ${error.message}")
                    return@forEach
                }
                rabbitTemplate.convertAndSend(queue.name, json)
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
