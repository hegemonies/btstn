package org.bravo.newsgrabber.property.app

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType
import org.bravo.newsgrabber.model.FetchStrategy
import org.bravo.newsgrabber.property.applicationPropertiesConfiguration

data class AppProperties(
    val strategy: FetchStrategy = FetchStrategy.valueOf(applicationPropertiesConfiguration[app.strategy])
)

private object app : PropertyGroup() {
    val strategy by stringType
}
