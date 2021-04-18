package org.bravo.newsgrabber.configuration

import com.github.badoualy.telegram.api.Kotlogram
import com.github.badoualy.telegram.api.TelegramApp
import com.github.badoualy.telegram.api.TelegramClient
import org.bravo.newsgrabber.configuration.properties.telegram.TelegramProperties
import org.bravo.newsgrabber.repository.TelegramStorage
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class TelegramClientConfiguration(
    private val telegramProperties: TelegramProperties,
    private val storage: TelegramStorage
) {

    @Bean
    fun telegramClient(): TelegramClient {
        val application = with(telegramProperties) {
            TelegramApp(
                apiId = apiId,
                apiHash = apiHash,
                appVersion = appVersion,
                deviceModel = clientDeviceModel,
                langCode = languageCode,
                systemVersion = clientDeviceSystemVersion
            )
        }

        return Kotlogram.getDefaultClient(
            application = application,
            apiStorage = storage
        )
    }
}


