package com.github.cpjinan.plugin.itemtools.hook

import org.bukkit.inventory.ItemStack

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.hook
 *
 * @author 季楠
 * @since 2025/5/4 22:29
 */
interface MythicMobsHook {
    /** 插件 MythicMobs 是否启用 **/
    fun isPluginEnabled(): Boolean

    /** 获取指定物品 **/
    fun getItem(id: String): ItemStack?

    /** 获取物品列表 **/
    fun getItemList(): Map<String, ItemStack>
}