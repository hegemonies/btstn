package org.bravo.newsgrabber.service.grabber

import org.bravo.newsgrabber.model.News
import org.bravo.newsgrabber.strategy.telegram.IFetchStrategy
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class TelegramGrabberProducer(
    private val rabbitTemplate: RabbitTemplate,
    private val queue: Queue
) {

    suspend fun grab(fetchStrategy: IFetchStrategy<Int, List<News>>, chatNumber: Int) {
        fetchStrategy.fetch(chatNumber).forEach { news ->
            rabbitTemplate.convertAndSend(queue.name, news) // TODO: подумать над сериализацией
        }
    }
}
