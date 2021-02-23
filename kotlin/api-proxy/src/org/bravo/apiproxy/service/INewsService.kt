package org.bravo.apiproxy.service

import arrow.core.Either
import org.bravo.apiproxy.model.Pagination
import org.bravo.apiproxy.model.ResponseError
import org.bravo.apiproxy.model.SearchNewsResponse

interface INewsService {

    suspend fun findNews(tag: String, pagination: Pagination): Either<ResponseError, SearchNewsResponse>
}
