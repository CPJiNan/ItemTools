package com.github.cpjinan.plugin.itemtools

import com.github.cpjinan.plugin.itemtools.ItemTools.plugin
import com.github.cpjinan.plugin.itemtools.utils.LoggerUtils
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.common.util.unsafeLazy
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/1 17:05
 */
object ItemToolsLoader {
    val api by unsafeLazy { DefaultItemToolsAPI() }

    /** 启动 ItemTools 服务 **/
    fun startup() {
        // 注册 API
        ItemTools.register(api)
    }

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        console().sendLang("Plugin-Loading", plugin.description.version)
        // 数据统计
        if (ItemToolsSettings.sendMetrics) Metrics(
            18992,
            BukkitPlugin.getInstance().description.version,
            Platform.BUKKIT
        )
    }

    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        LoggerUtils.message(
            "",
            "&o  ___ _               _____           _ ".colored(),
            "&o |_ _| |_ ___ _ __ __|_   _|__   ___ | |___ ".colored(),
            "&o  | || __/ _ \\ '_ ` _ \\| |/ _ \\ / _ \\| / __| ".colored(),
            "&o  | || ||  __/ | | | | | | (_) | (_) | \\__ \\ ".colored(),
            "&o |___|\\__\\___|_| |_| |_|_|\\___/ \\___/|_|___/ ".colored(),
            ""
        )
        console().sendLang("Plugin-Enabled")
    }

    @Awake(LifeCycle.DISABLE)
    fun onDisable() {
        console().sendLang("Plugin-Disable")
    }
}