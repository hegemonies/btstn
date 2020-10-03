package org.bravo.newsgrabber.repository

import com.github.badoualy.telegram.api.TelegramApiStorage
import com.github.badoualy.telegram.mtproto.auth.AuthKey
import com.github.badoualy.telegram.mtproto.model.DataCenter
import com.github.badoualy.telegram.mtproto.model.MTSession
import org.apache.commons.io.FileUtils
import java.io.File

class TelegramStorage(
    private val authKeyFile: File = File("src/resources/auth.key"),
    private val nearestDcFile: File = File("src/resources/dc.save")
) : TelegramApiStorage {

    override fun deleteAuthKey() {
        runCatching {
            FileUtils.forceDelete(authKeyFile)
        }.getOrElse { error ->
            println("Error delete auth key: ${error.message}")
        }
    }

    override fun deleteDc() {
        runCatching {
            FileUtils.forceDelete(nearestDcFile)
        }.getOrElse { error ->
            println("Error delete auth key: ${error.message}")
        }
    }

    override fun loadAuthKey(): AuthKey? =
        runCatching {
            AuthKey(FileUtils.readFileToByteArray(authKeyFile))
        }.getOrElse { error ->
            println("Error load auth key: ${error.message}")
            null
        }

    override fun loadDc(): DataCenter? =
        runCatching {
            FileUtils.readFileToString(nearestDcFile).split(":").let {
                DataCenter(it[0], Integer.parseInt(it[1]))
            }
        }.getOrElse { error ->
            println("Error load DC: ${error.message}")
            null
        }

    override fun saveAuthKey(authKey: AuthKey) {
        runCatching {
            FileUtils.writeByteArrayToFile(authKeyFile, authKey.key)
        }.getOrElse { error ->
            println("Error save auth key: ${error.message}")
        }
    }

    override fun saveDc(dataCenter: DataCenter) {
        runCatching {
            FileUtils.write(nearestDcFile, dataCenter.toString())
        }.getOrElse { error ->
            println("Error save DC: ${error.message}")
        }
    }

    override fun loadSession(): MTSession? {
        return null
    }

    override fun saveSession(session: MTSession?) {
    }
}