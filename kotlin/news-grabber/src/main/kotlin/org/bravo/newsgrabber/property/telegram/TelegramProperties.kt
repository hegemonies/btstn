package org.bravo.newsgrabber.property.telegram

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType
import org.bravo.newsgrabber.property.applicationPropertiesConfiguration

data class TelegramProperties(
    val appVersion: String = "0.1",
    val clientDeviceModel: String = "nomatterwhatmodel",
    val clientDeviceSystemVersion: String = "10",
    val languageCode: String = "en",
    val apiId: Int = applicationPropertiesConfiguration[telegram.apiId],
    val apiHash: String = applicationPropertiesConfiguration[telegram.apiHash],
    val phoneNumber: String = applicationPropertiesConfiguration[telegram.phoneNumber],
    val authKeyPath: String? = applicationPropertiesConfiguration.getOrNull(telegram.authKeyPath),
    val dataCenterPath: String? = applicationPropertiesConfiguration.getOrNull(telegram.dataCenterPath)
)

private object telegram : PropertyGroup() {
    val apiId by intType
    val apiHash by stringType
    val phoneNumber by stringType // syntax like "+79991237654"
    val authKeyPath by stringType
    val dataCenterPath by stringType
}
