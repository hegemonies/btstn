package org.bravo.apiproxy.service

import arrow.core.Validated

private const val tagLengthLimit = 10

object ValidationService {

    fun validateTag(tag: String): Validated<Exception, Unit> {

        val commonErrorMessage = "Tag failed validation"

        if (tag.isEmpty()) {
            return Validated.Invalid(Exception("$commonErrorMessage: tag is null"))
        }

        if (tag.length > tagLengthLimit) {
            return Validated.Invalid(Exception("$commonErrorMessage: tag is too long"))
        }

        // must contains only word symbols
        if (tag.contains(Regex("\\W|\\d"))) {
            return Validated.Invalid(Exception("$commonErrorMessage: tag contains forbidden characters"))
        }

        return Validated.Valid(Unit)
    }
}
