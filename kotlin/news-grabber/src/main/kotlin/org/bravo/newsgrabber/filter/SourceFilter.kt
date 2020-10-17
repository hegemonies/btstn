package org.bravo.newsgrabber.filter

import org.bravo.newsgrabber.property.app.AppProperties

private val appProperties = AppProperties()

fun sourceFilter(newsSource: String): Boolean =
    newsSource in appProperties.sources
