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
    /** 从配置构建物品 **/
    fun getItemFromConfig(config: ConfigurationSection): ItemStack
}