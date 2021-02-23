package org.bravo.apiproxy.model

data class CodeException(
    override val message: String,
    val code: ResponseError.Code = ResponseError.Code.UNKNOWN
) : Exception(message)
