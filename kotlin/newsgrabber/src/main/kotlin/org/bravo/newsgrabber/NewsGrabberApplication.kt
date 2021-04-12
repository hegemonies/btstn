package org.bravo.newsgrabber

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewsGrabberApplication

fun main(args: Array<String>) {
    runApplication<NewsGrabberApplication>(*args)
}
