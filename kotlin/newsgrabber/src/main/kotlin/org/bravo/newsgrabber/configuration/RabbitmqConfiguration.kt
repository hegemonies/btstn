package org.bravo.newsgrabber.configuration

import org.springframework.amqp.core.Queue
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
}
