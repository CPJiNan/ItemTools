package com.github.cpjinan.plugin.itemtools

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration

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

    /** 给予玩家物品 **/
    fun giveItem(player: Player, id: String, amount: Int)

    /** 获取指定物品 **/
    fun getItem(id: String): ItemStack?

    /** 获取物品列表 **/
    fun getItems(): HashMap<String, ItemStack>

    /** 获取物品名称列表 **/
    fun getItemNames(): List<String>

    /** 从配置文件构建物品 **/
    fun getItemFromConfig(config: ConfigurationSection): ItemStack

    /** 保存物品到配置文件 **/
    fun saveItemToConfig(item: ItemStack, config: Configuration, id: String)
}