package org.bravo.newscollector.configuration

import org.bravo.newscollector.service.NewsCollectorService
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitmqConfiguration {

    @Bean
    fun queue(): Queue {
        val queueName = "news-queue"
        val durable = true
        val exclusive = false
        val autoDelete = false

        return Queue(queueName, durable, exclusive, autoDelete)
    }

    @Bean
    fun messageListenerContainer(
        connectionFactory: ConnectionFactory,
        queue: Queue,
        newsCollectorService: NewsCollectorService
    ): MessageListenerContainer =
        SimpleMessageListenerContainer().apply {
            this.connectionFactory = connectionFactory
            setQueues(queue)
            setMessageListener(newsCollectorService)
        }
}
