package org.bravo.newsgrabber

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewsgrabberApplication

fun main(args: Array<String>) {
	runApplication<NewsgrabberApplication>(*args)
}
