package com.github.cpjinan.plugin.itemtools.hook.item

import org.bukkit.inventory.ItemStack

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.hook.item
 *
 * @author 季楠
 * @since 2025/6/21 17:30
 */
interface ItemSource {
    fun getItemStack(): ItemStack?
}