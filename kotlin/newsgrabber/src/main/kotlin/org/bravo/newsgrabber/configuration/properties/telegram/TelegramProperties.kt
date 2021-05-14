package org.bravo.newsgrabber.configuration.properties.telegram

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "telegram")
@Validated
class TelegramProperties {

    var appVersion: String = "0.2"

    var clientDeviceModel: String = "nomatterwhatmodel"

    var clientDeviceSystemVersion: String = "10"

    var languageCode: String = "en"

    var apiId: Int = 0

    @NotBlank
    lateinit var apiHash: String

    /**
     * Syntax like "+79991237654".
     */
    @NotBlank
    lateinit var phoneNumber: String

    var maxChannelsCount: Int = 50
}
