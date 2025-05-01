package com.github.cpjinan.plugin.itemtools

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/1 17:05
 */
interface ItemToolsAPI {
    /** 获取业务逻辑接口 **/
    fun getService(): ItemToolsService

    /** 获取插件挂钩接口 **/
    fun getHook(): ItemToolsHook

    /** 获取语言文件接口 **/
    fun getLanguage(): ItemToolsLanguage
}