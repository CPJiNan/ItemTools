package com.github.cpjinan.plugin.itemtools

import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/4 15:06
 */
interface ItemToolsManager {
    val item: HashMap<String, ItemStack>

    /** 重载物品配置 **/
    fun reload()

    /** 从配置文件构建物品 **/
    fun buildItem(config: ConfigurationSection): ItemStack
}