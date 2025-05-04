package com.github.cpjinan.plugin.itemtools

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.module.nms.getItemTag
import taboolib.module.nms.itemTagReader
import taboolib.platform.util.isAir
import taboolib.platform.util.modifyLore
import taboolib.platform.util.modifyMeta

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/1 17:54
 */
object DefaultItemToolsService : ItemToolsService {
    /** 获取物品 Lore **/
    override fun getLore(item: ItemStack): List<String> {
        return item.itemMeta.lore
    }

    /** 获取物品 Lore **/
    override fun getLore(item: ItemStack, index: Int): String {
        return getLore(item)[index]
    }

    /** 新增物品 Lore **/
    override fun addLore(item: ItemStack, element: String): ItemStack {
        return item.modifyLore {
            add(element)
        }
    }

    /** 新增物品 Lore **/
    override fun addLore(item: ItemStack, index: Int, element: String): ItemStack {
        return item.modifyLore {
            add(index, element)
        }
    }

    /** 删除物品 Lore **/
    override fun removeLore(item: ItemStack, index: Int): ItemStack {
        return item.modifyLore {
            removeAt(index)
        }
    }

    /** 设置物品 Lore **/
    override fun setLore(item: ItemStack, index: Int, element: String): ItemStack {
        return item.modifyLore {
            set(index, element)
        }
    }

    /** 获取物品附魔 **/
    override fun getEnchant(item: ItemStack): Map<String, Int> {
        return item.enchantments.mapKeys { it.key.name }
    }

    /** 设置物品附魔 **/
    override fun setEnchant(item: ItemStack, ench: String, level: Int) {
        item.addUnsafeEnchantment(Enchantment.getByName(ench), level)
    }

    /** 物品是否无法破坏 **/
    override fun isUnbreakable(item: ItemStack): Boolean {
        return item.itemMeta.isUnbreakable
    }

    /** 设置物品无法破坏 **/
    override fun setUnbreakable(item: ItemStack, bool: Boolean) {
        item.modifyMeta<ItemMeta> {
            this.isUnbreakable = bool
        }
    }

    /** 获取物品 NBT **/
    override fun getNBT(item: ItemStack, key: String): Any? {
        return item.getItemTag().getDeep(key)?.unsafeData()
    }

    /** 设置物品 NBT **/
    override fun setNBT(item: ItemStack, key: String, value: Any?) {
        item.itemTagReader {
            set(key, value)
            write(item)
        }
    }

    /** 物品是否为空气 **/
    override fun isAir(item: ItemStack?): Boolean {
        return item == null || item.isAir || item.type == Material.AIR
    }

    @Awake(LifeCycle.CONST)
    fun onConst() {
        // 注册服务
        PlatformFactory.registerAPI<ItemToolsService>(DefaultItemToolsService)
    }
}