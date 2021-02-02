package org.bravo.apiproxy.repository

import arrow.core.Either
import kotlinx.coroutines.newFixedThreadPoolContext
import org.bravo.apiproxy.model.Pagination
import org.bravo.model.dto.News
import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object NewsRepository {

    private val newsRepositoryDispatcher =
        newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "news-repository-dispatcher")

    /**
     * Find all news by tag.
     */
    suspend fun findByTag(tag: String): Either<Throwable, Pair<List<News>, Long>> =
        Either.catch {
            val (news, total) = newSuspendedTransaction(newsRepositoryDispatcher) {
                val query = selectNews(tag)

                val total = query.count()

                val news = query
                    .map { row ->
                        News.fromResultRow(row)
                    }

                news to total
            }

            news to total
        }

    /**
     * Find news by tag and pagination.
     */
    suspend fun findByTag(tag: String, pagination: Pagination): Either<Throwable, Pair<List<News>, Long>> =
        Either.catch {
            val (news, total) = newSuspendedTransaction(newsRepositoryDispatcher) {
                val query = selectNews(tag)

                val total = query.count()

                val news = query.limit(n = pagination.limit, offset = pagination.offset)
                    .map { row ->
                        News.fromResultRow(row)
                    }

                news to total
            }

            news to total
        }

    private fun selectNews(tag: String) =
        NewsTable.select { NewsTable.message like "%#$tag%" }
            .orderBy(NewsTable.date, SortOrder.DESC)
}
