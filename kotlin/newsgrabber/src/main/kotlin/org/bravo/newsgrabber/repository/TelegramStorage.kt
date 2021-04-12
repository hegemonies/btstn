package org.bravo.newsgrabber.repository

import com.github.badoualy.telegram.api.TelegramApiStorage
import com.github.badoualy.telegram.mtproto.auth.AuthKey
import com.github.badoualy.telegram.mtproto.model.DataCenter
import com.github.badoualy.telegram.mtproto.model.MTSession
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class TelegramStorage(
    applicationContext: ApplicationContext
) : TelegramApiStorage {

    private val authKeyFile = applicationContext.getResource("auth.key")
    private val dataCenterFile = applicationContext.getResource("dc.save")

    override fun deleteAuthKey() {
        runCatching {
            authKeyFile.file.delete()
        }.getOrElse { error ->
            logger.error("Can not delete auth.key file: ${error.message}")
        }
    }

    override fun deleteDc() {
        runCatching {
            dataCenterFile.file.delete()
        }.getOrElse { error ->
            logger.error("Can not delete dc.save file: ${error.message}")
        }
    }

    override fun loadAuthKey(): AuthKey? {
        return runCatching {
            AuthKey(authKeyFile.file.readBytes())
        }.getOrElse { error ->
            logger.error("Can not read auth.key file: ${error.message}")

            null
        }
    }

    override fun loadDc(): DataCenter? {
        TODO("Not yet implemented")
    }

    override fun loadSession(): MTSession? {
        TODO("Not yet implemented")
    }

    override fun saveAuthKey(authKey: AuthKey) {
        TODO("Not yet implemented")
    }

    override fun saveDc(dataCenter: DataCenter) {
        TODO("Not yet implemented")
    }

    override fun saveSession(session: MTSession?) {
        TODO("Not yet implemented")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
