package com.github.cpjinan.plugin.itemtools.network

import com.github.cpjinan.plugin.itemtools.utils.LoggerUtils.message
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.asLangTextList
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.asLangTextList
import java.net.HttpURLConnection
import java.net.URL

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.network
 *
 * @author 季楠
 * @since 2025/5/4 23:15
 */
object ItemToolsUpdate {
    fun sendConsoleNotice() {
        Thread {
            val urlConnection =
                URL("https://raw.githubusercontent.com/CPJiNan/ItemTools/refs/heads/master/src/main/kotlin/com/github/cpjinan/plugin/itemtools/network/Notice").openConnection() as HttpURLConnection
            try {
                val message = urlConnection.inputStream.bufferedReader().readText()
                if (message.isNotBlank()) message(message.colored())
            } catch (_: Throwable) {
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }

    fun sendPlayerNotice(player: Player) {
        Thread {
            val urlConnection =
                URL("https://raw.githubusercontent.com/CPJiNan/ItemTools/refs/heads/master/src/main/kotlin/com/github/cpjinan/plugin/itemtools/network/Notice").openConnection() as HttpURLConnection
            try {
                val message = urlConnection.inputStream.bufferedReader().readText()
                if (message.isNotBlank()) player.sendMessage(message.colored())
            } catch (_: Throwable) {
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }

    fun sendConsoleUpdate() {
        Thread {
            val urlConnection =
                URL("https://raw.githubusercontent.com/CPJiNan/ItemTools/refs/heads/master/src/main/kotlin/com/github/cpjinan/plugin/itemtools/network/Version").openConnection() as HttpURLConnection
            try {
                val latestVersion = urlConnection.inputStream.bufferedReader().readText()
                val currentVersion = BukkitPlugin.getInstance().description.version
                if (latestVersion != currentVersion) {
                    console().asLangTextList("Plugin-Update", latestVersion, currentVersion)
                        .forEach { message(it.colored()) }
                }
            } catch (_: Throwable) {
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }

    fun sendPlayerUpdate(player: Player) {
        Thread {
            val urlConnection =
                URL("https://raw.githubusercontent.com/CPJiNan/ItemTools/refs/heads/master/src/main/kotlin/com/github/cpjinan/plugin/itemtools/network/Version").openConnection() as HttpURLConnection
            try {
                val latestVersion = urlConnection.inputStream.bufferedReader().readText()
                val currentVersion = BukkitPlugin.getInstance().description.version
                if (latestVersion != currentVersion) {
                    player.asLangTextList("Plugin-Update", latestVersion, currentVersion)
                        .forEach { player.sendMessage(it.colored()) }
                }
            } catch (_: Throwable) {
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}