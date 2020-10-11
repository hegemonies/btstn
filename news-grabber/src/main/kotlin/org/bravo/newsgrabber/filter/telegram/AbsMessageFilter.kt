package org.bravo.newsgrabber.filter.telegram

import com.github.badoualy.telegram.tl.api.TLAbsMessage
import com.github.badoualy.telegram.tl.api.TLMessage
import com.github.badoualy.telegram.tl.api.TLMessageEmpty
import com.github.badoualy.telegram.tl.api.TLMessageService

fun isMessage(absMessage: TLAbsMessage) =
    when (absMessage) {
        is TLMessage -> true
        is TLMessageService -> false
        is TLMessageEmpty -> false
        else -> false // impossible case
    }
