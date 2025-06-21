package com.github.cpjinan.plugin.itemtools.hook.item

import com.github.cpjinan.plugin.itemtools.ItemTools
import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.hook.item
 *
 * @author 季楠
 * @since 2025/6/21 17:30
 */
class ItemToolsItemSource(val config: ConfigurationSection) : ItemSource {
    override fun getItemStack(): ItemStack? {
        return ItemTools.api().getManager().getItemFromConfig(config)
    }
}