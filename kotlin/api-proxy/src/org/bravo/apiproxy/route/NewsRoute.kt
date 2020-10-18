package org.bravo.apiproxy.route

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import org.bravo.apiproxy.model.SearchNewsRequest
import org.bravo.apiproxy.model.SearchNewsResponse
import org.bravo.apiproxy.model.SearchNewsResponseView
import org.bravo.model.mapper.mapToNewsDto
import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransaction
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("NewsRoute")

fun Route.news() {
    post("/news") {
        logger.debug("[${this.call.callId}] Start handle /news request")

        val searchNews = call.receive<SearchNewsRequest>()

        logger.debug("[${this.call.callId}] Searching news by tag #${searchNews.tag}...")

        newSuspendedTransaction(Dispatchers.Default) {
            suspendedTransaction {
                NewsTable.select { NewsTable.message like "%#${searchNews.tag}%" }.map {
                    mapToNewsDto(it)
                }.map { dto ->
                    SearchNewsResponseView.fromNewsDto(dto)
                }.let { view ->
                    SearchNewsResponse(view)
                }.also {
                    logger.debug("[${call.callId}] Searched news by tag #${searchNews.tag}: $it")

                    call.respond(it)
                }
            }
        }

        logger.debug("[${call.callId}] Finish handle /news request")
    }
}
