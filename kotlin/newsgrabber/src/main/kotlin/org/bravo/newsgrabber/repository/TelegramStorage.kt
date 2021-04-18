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
        logger.info("Deleting auth key")

        runCatching {
            authKeyFile.file.delete()
        }.getOrElse { error ->
            logger.error("Can not delete auth.key file: ${error.message}")
        }
    }

    override fun deleteDc() {
        logger.info("Deleting datacenter file")

        runCatching {
            dataCenterFile.file.delete()
        }.getOrElse { error ->
            logger.error("Can not delete dc.save file: ${error.message}")
        }
    }

    override fun loadAuthKey(): AuthKey? {
        logger.info("Loading auth key")

        return runCatching {
            AuthKey(authKeyFile.file.readBytes())
        }.getOrElse { error ->
            logger.error("Can not read auth.key file: ${error.message}")

            null
        }
    }

    override fun loadDc(): DataCenter? {
        logger.info("Loading datacenter file")

        return runCatching {
            dataCenterFile.file.readBytes().toString(charset("UTF-8")).split(":").let {
                DataCenter(it[0], Integer.parseInt(it[1]))
            }
        }.getOrElse { error ->
            logger.error("Can not read dc.save file: ${error.message}")

            null
        }
    }

    override fun loadSession(): MTSession? {
        logger.info("Loading session")

        return null
    }

    override fun saveAuthKey(authKey: AuthKey) {
        logger.info("Saving auth key")

        runCatching {
            authKeyFile.file.writeText(authKey.key.toString())
        }.getOrElse { error ->
            logger.error("Can not save auth key: ${error.message}")
        }
    }

    override fun saveDc(dataCenter: DataCenter) {
        logger.info("Saving datacenter file")

        runCatching {
            dataCenterFile.file.writeText(dataCenter.toString())
        }.getOrElse { error ->
            logger.error("Can not save datacenter file: ${error.message}")
        }
    }

    override fun saveSession(session: MTSession?) {}

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }
}
