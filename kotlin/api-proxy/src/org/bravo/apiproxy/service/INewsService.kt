package org.bravo.apiproxy.service

import arrow.core.Either
import org.bravo.apiproxy.model.Pagination
import org.bravo.apiproxy.model.SearchNewsResponse

interface INewsService {

    suspend fun findNews(tag: String, pagination: Pagination): Either<Exception, SearchNewsResponse>

    suspend fun findAllNews(tag: String): Either<Exception, SearchNewsResponse>
}
