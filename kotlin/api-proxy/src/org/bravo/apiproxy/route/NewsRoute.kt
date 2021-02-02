package org.bravo.apiproxy.route

import arrow.core.Either
import arrow.core.getOrHandle
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.bravo.apiproxy.const.DEFAULT_UNKNOWN_ERROR_MESSAGE
import org.bravo.apiproxy.exception.ClientException
import org.bravo.apiproxy.exception.ServerException
import org.bravo.apiproxy.model.Response
import org.bravo.apiproxy.model.ResponseError
import org.bravo.apiproxy.model.SearchNewsRequest
import org.bravo.apiproxy.service.NewsService
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("NewsRoute")

fun Route.news() {
    get("/newsAll") {
        logger.debug(
            "[${this.call.callId}] Start handle ${call.request.path()} request " +
                "from ${call.request.local.host} ${call.request.origin.host}"
        )

        val tag = call.request.queryParameters["tag"]
            ?: run {
                "Can not find news: can not get 'tag' query parameter".also { errorMessage ->
                    logger.warn(errorMessage)
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = ResponseError(errorMessage)
                    )
                }

                return@get
            }

        handleResponse(NewsService.findAllNews(tag)).also { (statusCode, response) ->
            call.respond(statusCode, response)
        }
    }

    post("/news") {
        logger.debug(
            "[${this.call.callId}] Start handle ${call.request.path()} request " +
                "from ${call.request.local.host} ${call.request.origin.host}"
        )

        val requestBody = Either.catch {
            call.receive<SearchNewsRequest>()
        }.getOrHandle { error ->
            "Can not parse request body to model: ${error.message}".also { errorMessage ->
                logger.warn(errorMessage)
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = ResponseError(errorMessage)
                )
            }

            return@post
        }

        handleResponse(NewsService.findNews(requestBody.tag, requestBody.pagination)).also { (statusCode, response) ->
            call.respond(statusCode, response)
        }
    }
}

private fun handleResponse(response: Either<Exception, Response>): Pair<HttpStatusCode, Response> =
    when (response) {
        is Either.Left ->
            when (response.a) {
                is ClientException ->
                    HttpStatusCode.BadRequest to ResponseError(
                        response.a.message ?: DEFAULT_UNKNOWN_ERROR_MESSAGE
                    )

                is ServerException ->
                    HttpStatusCode.InternalServerError to ResponseError(
                        response.a.message ?: DEFAULT_UNKNOWN_ERROR_MESSAGE
                    )

                else ->
                    HttpStatusCode.InternalServerError to ResponseError(
                        response.a.message ?: DEFAULT_UNKNOWN_ERROR_MESSAGE
                    )
            }

        is Either.Right ->
            HttpStatusCode.OK to response.b
    }
