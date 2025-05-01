package com.github.cpjinan.plugin.itemtools

import taboolib.common.LifeCycle
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.disablePlugin
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.platform.BukkitPlugin

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/1 17:05
 */
object ItemTools : Plugin() {
    val plugin by lazy { BukkitPlugin.getInstance() }
    private var api: ItemToolsAPI? = null

    init {
        registerLifeCycleTask(LifeCycle.INIT) {
            try {
                ItemToolsLoader.startup()
            } catch (ex: Throwable) {
                ex.printStackTrace()
                disablePlugin()
            }
        }
    }

    /** 获取开发者接口 **/
    fun api(): ItemToolsAPI {
        return api ?: error("ItemToolsAPI has not finished loading, or failed to load!")
    }

    /** 注册开发者接口 **/
    fun register(api: ItemToolsAPI) {
        ItemTools.api = api
    }
}