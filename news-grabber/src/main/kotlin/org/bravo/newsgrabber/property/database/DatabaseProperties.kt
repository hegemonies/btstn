package org.bravo.newsgrabber.property.database

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType
import org.bravo.newsgrabber.property.applicationPropertiesConfiguration

data class DatabaseProperties(
    val host: String = applicationPropertiesConfiguration[database.host],
    val username: String = applicationPropertiesConfiguration[database.username],
    val password: String = applicationPropertiesConfiguration[database.password],
    val port: Int = applicationPropertiesConfiguration[database.port]
)

private object database : PropertyGroup() {
    val host by stringType
    val port by intType
    val username by stringType
    val password by stringType
}
