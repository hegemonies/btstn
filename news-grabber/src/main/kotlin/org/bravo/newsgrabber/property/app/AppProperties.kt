package org.bravo.newsgrabber.property.app

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType
import org.bravo.newsgrabber.model.FetchStrategy
import org.bravo.newsgrabber.property.applicationPropertiesConfiguration

data class AppProperties(
    val strategy: FetchStrategy = FetchStrategy.valueOf(applicationPropertiesConfiguration[app.strategy]),
    val sources: List<String> = applicationPropertiesConfiguration[app.sources].split(";")
)

private object app : PropertyGroup() {
    val strategy by stringType
    val sources by stringType
}
