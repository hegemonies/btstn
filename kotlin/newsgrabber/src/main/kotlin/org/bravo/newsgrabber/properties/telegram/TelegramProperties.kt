package org.bravo.newsgrabber.properties.telegram

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Configuration
@ConfigurationProperties(prefix = "telegram")
@Validated
class TelegramProperties {

    val appVersion: String = "0.2"

    val clientDeviceModel: String = "nomatterwhatmodel"

    val clientDeviceSystemVersion: String = "10"

    val languageCode: String = "en"

    val apiId: Int = 0

    @NotBlank
    lateinit var apiHash: String

    /**
     * syntax be like "+79991237654"
     */
    @NotBlank
    lateinit var phoneNumber: String
}
