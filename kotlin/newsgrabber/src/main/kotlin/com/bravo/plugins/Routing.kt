package com.bravo.plugins

import arrow.core.Either
import arrow.core.Either.Companion
import arrow.core.getOrHandle
import com.bravo.channel.telegramGrabberChannel
import com.bravo.config.telegramGrabberCoroutineScope
import com.bravo.dto.SaveNewsRequest
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.launch
import org.bravo.model.dto.News
import org.bravo.model.dto.ResponseError
import org.slf4j.LoggerFactory

fun Application.configureRouting() {


    routing {

        /**
         * Принимает запросы от python сервиса сбора новостей из telegram каналов.
         */
        post("/news") {

            val request = Either.catch {
                call.receive<SaveNewsRequest>()
            }.getOrHandle { error ->
                val errorMessage = "Can not parse request body to model"

                log.warn("$errorMessage: ${error.message}")

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

            telegramGrabberCoroutineScope.launch {
                if (request.messageContent != null) {
                    telegramGrabberChannel.send(
                        News(
                            message = request.messageContent,
                            source = request.channelName,
                            objectId = request.messageId.toLong(),
                            date = request.date.toFloatOrNull()?.toLong()?.times(1000) ?: 0L
                        )
                    )
                }
            }
        }

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
