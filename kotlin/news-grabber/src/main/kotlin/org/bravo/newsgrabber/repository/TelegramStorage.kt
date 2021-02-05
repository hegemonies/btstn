package org.bravo.newsgrabber.repository

import com.github.badoualy.telegram.api.TelegramApiStorage
import com.github.badoualy.telegram.mtproto.auth.AuthKey
import com.github.badoualy.telegram.mtproto.model.DataCenter
import com.github.badoualy.telegram.mtproto.model.MTSession
import org.apache.commons.io.FileUtils
import org.bravo.newsgrabber.property.telegram.TelegramProperties
import org.slf4j.LoggerFactory
import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TelegramStorage : TelegramApiStorage {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val telegramProperties = TelegramProperties()

    private val authKeyFile: File =
        File(
            telegramProperties.authKeyPath
                ?: runCatching {
                    this::class.java.classLoader.getResource("auth.key").path
                }.getOrElse {
                    logger.warn("The path to the auth.key file was not set. Creating a new one.")

                    val propertiesFileName = "application.properties"
                    val authKeyFileName = "auth.key"
                    val filePrefix = "file:"
                    val applicationPropertiesFile = this::class.java.classLoader.getResource(propertiesFileName)
                    val authKeyFile =
                        applicationPropertiesFile.toString()
                            .removePrefix(filePrefix)
                            .removeSuffix(propertiesFileName)
                            .plus(authKeyFileName)

                    File(authKeyFile).createNewFile()

                    authKeyFile
                }
        )

    private val nearestDcFile: File =
        File(
            telegramProperties.dataCenterPath
                ?: runCatching {
                    this::class.java.classLoader.getResource("dc.save").path
                }.getOrElse {
                    logger.warn("The path to the dc.save file was not set. Creating a new one.")

                    val propertiesFileName = "application.properties"
                    val dcFileName = "dc.save"
                    val filePrefix = "file:"
                    val applicationPropertiesFile = this::class.java.classLoader.getResource(propertiesFileName)
                    val dcFile =
                        applicationPropertiesFile.toString()
                            .removePrefix(filePrefix)
                            .removeSuffix(propertiesFileName)
                            .plus(dcFileName)

                    File(dcFile).createNewFile()

                    dcFile
                }
        )

    override fun deleteAuthKey() {

        runCatching {
            FileUtils.forceDelete(authKeyFile)
        }.getOrElse { error ->
            logger.error("Error delete auth key: ${error.message}")
        }
    }

    override fun deleteDc() {
        runCatching {
            FileUtils.forceDelete(nearestDcFile)
        }.getOrElse { error ->
            logger.error("Error delete auth key: ${error.message}")
        }
    }

    override fun loadAuthKey(): AuthKey? =
        runCatching {
            AuthKey(FileUtils.readFileToByteArray(authKeyFile))
        }.getOrElse { error ->
            logger.error("Error load auth key: ${error.message}")
            null
        }

    override fun loadDc(): DataCenter? =
        runCatching {
            FileUtils.readFileToString(nearestDcFile).split(":").let {
                DataCenter(it[0], Integer.parseInt(it[1]))
            }
        }.getOrElse { error ->
            logger.error("Error load DC: ${error.message}")
            null
        }

    override fun saveAuthKey(authKey: AuthKey) {
        runCatching {
            FileUtils.writeByteArrayToFile(authKeyFile, authKey.key)
        }.getOrElse { error ->
            logger.error("Error save auth key: ${error.message}")
        }
    }

    override fun saveDc(dataCenter: DataCenter) {
        runCatching {
            FileUtils.write(nearestDcFile, dataCenter.toString())
        }.getOrElse { error ->
            logger.error("Error save DC: ${error.message}")
        }
    }

    override fun loadSession(): MTSession? {
        return null
    }

    override fun saveSession(session: MTSession?) {
    }
}