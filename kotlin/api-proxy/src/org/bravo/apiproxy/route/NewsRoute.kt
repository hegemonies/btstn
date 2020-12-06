package org.bravo.apiproxy.route

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bravo.apiproxy.model.SearchNewsRequest
import org.bravo.apiproxy.repository.NewsRepository
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("NewsRoute")

fun Route.news() {
    post("/news") {
        logger.debug("[${this.call.callId}] Start handle /news request")

        val searchNews = call.receive<SearchNewsRequest>()

        logger.debug("[${this.call.callId}] Searching news by tag #${searchNews.tag}...")

        call.respond(NewsRepository.findAllByTag(searchNews.tag))

        logger.debug("[${call.callId}] Finish handle /news request")
    }
}
