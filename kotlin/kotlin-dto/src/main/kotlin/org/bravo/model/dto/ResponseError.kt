package org.bravo.model.dto

import org.bravo.const.DEFAULT_UNKNOWN_ERROR_MESSAGE


data class ResponseError(
    val message: String,
    val cause: String? = DEFAULT_UNKNOWN_ERROR_MESSAGE,
    val code: Code = Code.UNKNOWN
) : Response {

    enum class Code {
        UNKNOWN,
        INVALID_TAG,
        SEVERAL_TAG,
        NOT_EXISTS_TAG,
        DATABASE_ERROR,
        PARSING_ERROR,
        ;

        fun isClientError() =
            when (this) {
                INVALID_TAG, SEVERAL_TAG, NOT_EXISTS_TAG, PARSING_ERROR -> true
                else -> false
            }

        fun isServerError() =
            when (this) {
                UNKNOWN, DATABASE_ERROR -> true
                else -> false
            }
    }

    fun stackMessagesAndRewrite(message: String) =
        this.copy(
            message = message,
            cause = "$message: $cause",
        )

    companion object {

        fun fromCodeException(
            errorMessage: String,
            codeException: CodeException
        ) =
            ResponseError(
                message = errorMessage,
                cause = codeException.message,
                code = codeException.code
            )
    }
}
