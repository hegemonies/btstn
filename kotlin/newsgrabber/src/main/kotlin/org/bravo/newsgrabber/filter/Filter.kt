package org.bravo.newsgrabber.filter

import com.github.badoualy.telegram.tl.api.TLAbsMessage
import com.github.badoualy.telegram.tl.api.TLMessage
import com.github.badoualy.telegram.tl.api.TLMessageEmpty
import com.github.badoualy.telegram.tl.api.TLMessageService
import org.bravo.newsgrabber.configuration.properties.app.AppProperties
import org.springframework.stereotype.Component

@Component
class Filter(
    private val appProperties: AppProperties
) {

    fun sourceFilter(newsSource: String): Boolean =
        newsSource in appProperties.sources

    fun tagFilter(message: String) =
        message.contains("#") || message.contains("$")

    fun absMessageFilter(absMessage: TLAbsMessage) =
        when (absMessage) {
            is TLMessage -> true
            is TLMessageService -> false
            is TLMessageEmpty -> false
            else -> false // impossible case
        }
}
