package com.github.cpjinan.plugin.itemtools

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/1 17:53
 */
object DefaultItemToolsHook : ItemToolsHook {
    @Awake(LifeCycle.CONST)
    fun onConst() {
        PlatformFactory.registerAPI<ItemToolsHook>(DefaultItemToolsHook)
    }
}