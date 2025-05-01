package com.github.cpjinan.plugin.itemtools.utils

import com.github.cpjinan.plugin.itemtools.ItemToolsSettings
import taboolib.module.chat.colored
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.*

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.utils
 *
 * @author 季楠
 * @since 2025/5/1 17:05
 */
object LoggerUtils {
    @JvmStatic
    fun message(vararg message: String) {
        for (i in message) {
            BukkitPlugin.getInstance().server.consoleSender.sendMessage(i)
        }
    }

    @JvmStatic
    fun info(vararg message: String) {
        for (i in message) {
            BukkitPlugin.getInstance().server.consoleSender.sendInfo(i)
        }
    }

    @JvmStatic
    fun warn(vararg message: String) {
        for (i in message) {
            BukkitPlugin.getInstance().server.consoleSender.sendWarn(i)
        }
    }

    @JvmStatic
    fun error(vararg message: String) {
        for (i in message) {
            BukkitPlugin.getInstance().server.consoleSender.sendError(i)
        }
    }

    @JvmStatic
    fun infoMessage(vararg message: String) {
        for (i in message) {
            BukkitPlugin.getInstance().server.consoleSender.sendInfoMessage(i)
        }
    }

    @JvmStatic
    fun warnMessage(vararg message: String) {
        for (i in message) {
            BukkitPlugin.getInstance().server.consoleSender.sendWarnMessage(i)
        }
    }

    @JvmStatic
    fun errorMessage(vararg message: String) {
        for (i in message) {
            BukkitPlugin.getInstance().server.consoleSender.sendErrorMessage(i)
        }
    }

    @JvmStatic
    fun debug(vararg message: String) {
        if (ItemToolsSettings.debug) {
            message.forEach {
                BukkitPlugin.getInstance().server.consoleSender.sendMessage(it.colored())
            }
        }
    }
}