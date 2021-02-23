package org.bravo.apiproxy.service

import arrow.core.Either
import arrow.core.handleError
import org.bravo.apiproxy.model.Pagination
import org.bravo.apiproxy.model.ResponseError
import org.bravo.apiproxy.model.SearchNewsResponse
import org.bravo.apiproxy.model.SearchNewsResponseView
import org.bravo.apiproxy.repository.NewsRepository

object NewsService : INewsService {

    override suspend fun findNews(tag: String, pagination: Pagination): Either<ResponseError, SearchNewsResponse> {

        val commonErrorMessage = "Can not find news"

        ValidationService.validateTag(tag).handleError { error ->
            return Either.left(
                ResponseError(
                    message = commonErrorMessage,
                    cause = error.message,
                    code = ResponseError.Code.INVALID_TAG
                )
            )
        }

        return when (val news = NewsRepository.findByTag(tag, pagination)) {
            is Either.Left ->
                Either.left(news.a)

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
