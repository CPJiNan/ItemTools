package com.github.cpjinan.plugin.itemtools

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/1 17:54
 */
object DefaultItemToolsService : ItemToolsService {
    @Awake(LifeCycle.CONST)
    fun onConst() {
        // 注册服务
        PlatformFactory.registerAPI<ItemToolsService>(DefaultItemToolsService)
    }
}