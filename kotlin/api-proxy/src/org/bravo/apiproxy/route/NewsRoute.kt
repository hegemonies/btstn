package org.bravo.apiproxy.route

import arrow.core.Either
import arrow.core.getOrHandle
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bravo.apiproxy.model.Response
import org.bravo.apiproxy.model.ResponseError
import org.bravo.apiproxy.model.SearchNewsRequest
import org.bravo.apiproxy.service.NewsService
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("NewsRoute")

fun Route.news() {

    post("/api/news") {
        logger.debug(
            "[${this.call.callId}] Start handle ${call.request.path()} request " +
                "from ${call.request.local.host} ${call.request.origin.host}"
        )

        val requestBody = Either.catch {
            call.receive<SearchNewsRequest>()
        }.getOrHandle { error ->
            val errorMessage = "Can not parse request body to model"

            logger.warn("$errorMessage: ${error.message}")

            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ResponseError(
                    message = errorMessage,
                    cause = error.message,
                    code = ResponseError.Code.PARSING_ERROR
                )
            )

            return@post
        }

        handleResponse(NewsService.findNews(requestBody.tag, requestBody.pagination)).also { (statusCode, response) ->
            call.respond(statusCode, response)
        }
    }
}

private fun handleResponse(response: Either<ResponseError, Response>): Pair<HttpStatusCode, Response> {
    val errorMessage = "Can not find news"

    return when (response) {
        is Either.Left ->
            when {
                response.value.code.isClientError() ->
                    HttpStatusCode.BadRequest to response.value

                response.value.code.isServerError() ->
                    HttpStatusCode.InternalServerError to response.value

                else ->
                    HttpStatusCode.InternalServerError to ResponseError(
                        message = errorMessage,
                        cause = response.value.message
                    )
            }

        is Either.Right ->
            HttpStatusCode.OK to response.value
    }
}
