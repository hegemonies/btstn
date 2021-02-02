package org.bravo.apiproxy.service

import arrow.core.Either
import arrow.core.handleError
import org.bravo.apiproxy.exception.ClientException
import org.bravo.apiproxy.exception.ServerException
import org.bravo.apiproxy.model.Pagination
import org.bravo.apiproxy.model.SearchNewsResponse
import org.bravo.apiproxy.model.SearchNewsResponseView
import org.bravo.apiproxy.repository.NewsRepository

object NewsService : INewsService {

    override suspend fun findNews(tag: String, pagination: Pagination): Either<Exception, SearchNewsResponse> {

        val commonErrorMessage = "Can not find news"

        ValidationService.validateTag(tag).also { validated ->
            validated.takeIf { it.isInvalid }.also {
                validated.handleError { error ->
                    return Either.left(ClientException("$commonErrorMessage: ${error.message}"))
                }
            }
        }

        return when (val news = NewsRepository.findByTag(tag, pagination)) {
            is Either.Left ->
                Either.left(ServerException("$commonErrorMessage: ${news.a}"))

            is Either.Right ->
                news.b.let { (newsList, total) ->
                    Either.right(
                        SearchNewsResponse(
                            view = newsList.map { SearchNewsResponseView.fromNews(it) },
                            total = total
                        )
                    )
                }
        }
    }

    override suspend fun findAllNews(tag: String): Either<Exception, SearchNewsResponse> {

        val commonErrorMessage = "Can not find news"

        ValidationService.validateTag(tag).also { validated ->
            validated.takeIf { it.isInvalid }.also {
                validated.handleError { error ->
                    return Either.left(ClientException("$commonErrorMessage: ${error.message}"))
                }
            }
        }

        return when (val news = NewsRepository.findByTag(tag)) {
            is Either.Left ->
                Either.left(ServerException("$commonErrorMessage: ${news.a}"))

            is Either.Right ->
                news.b.let { (newsList, total) ->
                    Either.right(
                        SearchNewsResponse(
                            view = newsList.map { SearchNewsResponseView.fromNews(it) },
                            total = total
                        )
                    )
                }
        }
    }
}
