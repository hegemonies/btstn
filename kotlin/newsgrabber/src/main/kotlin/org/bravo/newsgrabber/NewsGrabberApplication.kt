package org.bravo.newsgrabber

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class NewsGrabberApplication

fun main(args: Array<String>) {
    runApplication<NewsGrabberApplication>(*args)
}
