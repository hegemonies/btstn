package org.bravo.apiproxy.repository

import arrow.core.Either
import arrow.core.Validated
import arrow.core.handleError
import kotlinx.coroutines.newFixedThreadPoolContext
import org.bravo.apiproxy.const.DEFAULT_UNKNOWN_ERROR_MESSAGE
import org.bravo.apiproxy.model.CodeException
import org.bravo.apiproxy.model.Pagination
import org.bravo.apiproxy.model.ResponseError
import org.bravo.model.dto.News
import org.bravo.model.dto.Tag
import org.bravo.model.table.NewsTable
import org.bravo.model.table.NewsTagsTable
import org.bravo.model.table.TagsTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object NewsRepository {

    private val newsRepositoryDispatcher =
        newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "news-repository-dispatcher")

    /**
     * Find news by tag and pagination.
     */
    suspend fun findByTag(tagName: String, pagination: Pagination): Either<ResponseError, Pair<List<News>, Long>> {
        return newSuspendedTransaction(newsRepositoryDispatcher) {
            val errorMessage = "Can not find news by tag"

            getTagModel(tagName).handleError { error ->
                return@newSuspendedTransaction Either.Left(ResponseError.fromCodeException(errorMessage, error))
            }

            val selectNewsQuery = runCatching {
                findNewsByTagQuery(tagName)
            }.getOrElse { error ->
                return@newSuspendedTransaction Either.Left(
                    ResponseError(
                        message = errorMessage,
                        cause = error.message,
                        code = ResponseError.Code.DATABASE_ERROR
                    )
                )
            }

            val total = selectNewsQuery.count()

            val news = selectNewsQuery.limit(n = pagination.limit, offset = pagination.offset)
                .map { row ->
                    News.fromResultRow(row)
                }

            Either.Right(
                news to total
            )
        }
    }

    private fun findNewsByTagQuery(tagName: String) =
        NewsTable.innerJoin(NewsTagsTable)
            .innerJoin(TagsTable)
            .slice(NewsTable.columns)
            .select {
                TagsTable.tag eq tagName.toLowerCase()
            }
            .orderBy(NewsTable.date, SortOrder.DESC)

    private fun selectTag(tag: String) =
        TagsTable.select { TagsTable.tag eq tag.toLowerCase() }
            .map { row ->
                Tag.fromResultRow(row)
            }

    private fun getTagModel(tag: String): Validated<CodeException, Tag> {
        val tags = runCatching {
            selectTag(tag)
        }.getOrElse { error ->
            return Validated.Invalid(
                CodeException(
                    message = error.message ?: DEFAULT_UNKNOWN_ERROR_MESSAGE,
                    code = ResponseError.Code.DATABASE_ERROR
                )
            )
        }

        if (tags.isEmpty()) {
            return Validated.Invalid(
                CodeException(
                    message = "Not found such tag as $tag",
                    code = ResponseError.Code.NOT_EXISTS_TAG
                )
            )
        }

        if (tags.size > 1) {
            return Validated.Invalid(
                CodeException(
                    message = "Find several tags for $tag: $tags",
                    code = ResponseError.Code.SEVERAL_TAG
                )
            )
        }

        return Validated.Valid(tags.first())
    }
}
