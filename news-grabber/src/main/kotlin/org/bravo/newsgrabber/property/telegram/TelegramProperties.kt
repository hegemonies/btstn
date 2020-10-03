package org.bravo.newsgrabber.property.telegram

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.intType
import com.natpryce.konfig.overriding
import com.natpryce.konfig.stringType

data class TelegramProperties(
    val appVersion: String = "0.1",
    val clientDeviceModel: String = "nomatterwhatmodel",
    val clientDeviceSystemVersion: String = "10",
    val languageCode: String = "en",
    val apiId: Int = telegramConfiguration[telegramProperties.apiId],
    val apiHash: String = telegramConfiguration[telegramProperties.apiHash],
    val phoneNumber: String = telegramConfiguration[telegramProperties.phoneNumber]
)

private val telegramConfiguration = ConfigurationProperties.systemProperties() overriding
    EnvironmentVariables() overriding
    ConfigurationProperties.fromResource("application.properties")

private object telegramProperties : PropertyGroup() {
    val apiId by intType
    val apiHash by stringType
    val phoneNumber by stringType // syntax like "+79991237654"
}
