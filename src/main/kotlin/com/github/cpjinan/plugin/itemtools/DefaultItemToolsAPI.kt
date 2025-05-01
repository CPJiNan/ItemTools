package com.github.cpjinan.plugin.itemtools

import taboolib.common.platform.PlatformFactory

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/1 17:05
 */
class DefaultItemToolsAPI : ItemToolsAPI {
    /** 业务逻辑接口 **/
    var localService = PlatformFactory.getAPI<ItemToolsService>()

    /** 插件挂钩接口 **/
    var localHook = PlatformFactory.getAPI<ItemToolsHook>()

    /** 语言拓展接口 **/
    var localLanguage = PlatformFactory.getAPI<ItemToolsLanguage>()

    /** 获取业务逻辑接口 **/
    override fun getService(): ItemToolsService {
        return localService
    }

    /** 获取插件挂钩接口 **/
    override fun getHook(): ItemToolsHook {
        return localHook
    }

    /** 获取语言文件接口 **/
    override fun getLanguage(): ItemToolsLanguage {
        return localLanguage
    }
}