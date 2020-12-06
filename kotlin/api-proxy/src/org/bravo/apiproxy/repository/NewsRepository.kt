package org.bravo.apiproxy.repository

import kotlinx.coroutines.newFixedThreadPoolContext
import org.bravo.apiproxy.model.SearchNewsResponse
import org.bravo.apiproxy.model.SearchNewsResponseView
import org.bravo.model.table.NewsTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object NewsRepository {

    private val newsRepositoryDispatcher =
        newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "news-repository-dispatcher")

    suspend fun findAllByTag(tag: String): SearchNewsResponse =
        newSuspendedTransaction(newsRepositoryDispatcher) {
            NewsTable.select { NewsTable.message like "%#$tag%" }
                .orderBy(NewsTable.date, SortOrder.DESC)
                .map { row ->
                    SearchNewsResponseView.fromResultRow(row)
                }
                .let { view ->
                    SearchNewsResponse(view)
                }
        }
}