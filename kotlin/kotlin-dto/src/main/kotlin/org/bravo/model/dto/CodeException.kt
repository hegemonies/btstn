package org.bravo.model.dto

data class CodeException(
    override val message: String,
    val code: ResponseError.Code = ResponseError.Code.UNKNOWN
) : Exception(message)
