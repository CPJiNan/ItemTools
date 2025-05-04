package com.github.cpjinan.plugin.itemtools.hook

import ink.ptms.um.Mythic
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.hook
 *
 * @author 季楠
 * @since 2025/5/4 22:28
 */
object DefaultMythicMobsHook : MythicMobsHook {
    /** 插件 MythicMobs 是否启用 **/
    override fun isPluginEnabled(): Boolean {
        return Bukkit.getPluginManager().isPluginEnabled("MythicMobs")
    }

    /** 获取指定物品 **/
    override fun getItem(id: String): ItemStack? {
        return Mythic.API.getItemStack(id)
    }

    /** 获取物品列表 **/
    override fun getItemList(): Map<String, ItemStack> {
        return Mythic.API.getItemIDList().associateWith { getItem(it)!! }
    }
}