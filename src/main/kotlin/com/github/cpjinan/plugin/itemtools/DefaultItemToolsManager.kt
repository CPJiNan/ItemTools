package com.github.cpjinan.plugin.itemtools

import org.bukkit.Color.fromRGB
import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.itemTagReader
import taboolib.platform.util.buildItem

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/4 15:08
 */
object DefaultItemToolsManager : ItemToolsManager {
    /** 从配置构建物品 **/
    override fun getItemFromConfig(config: ConfigurationSection): ItemStack {
        val material = config.getString("Type", "AIR")!!
        val damage = config.getInt("Data", 0)
        val name = config.getString("Display", "")!!
        val lore = config.getStringList("Lore")
        val enchants = config.getConfigurationSection("Enchantments")!!
        val flags = config.getStringList("Options.HideFlags")
        val shiny = config.getBoolean("Options.Glow", false)
        val unbreakable = config.getBoolean("Options.Unbreakable", false)
        val originMeta = config.getConfigurationSection("Options")!!
        val nbt = config.getConfigurationSection("NBT")!!
        return material(material).damage(damage).name(name).lore(lore).enchants(enchants)
            .flags(flags).shiny(shiny).unbreakable(unbreakable).originMeta(originMeta).colored().nbt(nbt)
    }

    fun material(material: String): ItemStack {
        return buildItem(XMaterial.valueOf(material))
    }

    fun ItemStack.damage(damage: Int): ItemStack {
        return buildItem(this) {
            this.damage = damage
        }
    }

    fun ItemStack.name(name: String): ItemStack {
        return buildItem(this) {
            this.name = name
        }
    }

    fun ItemStack.lore(lore: List<String>): ItemStack {
        return buildItem(this) {
            this.lore.addAll(lore)
        }
    }

    fun ItemStack.enchants(enchant: ConfigurationSection): ItemStack {
        return buildItem(this) {
            enchant.getKeys(false).forEach {
                val ench = Enchantment.getByName(it) ?: return@forEach
                this.enchants[ench] = enchant.getInt(it)
            }
        }
    }

    fun ItemStack.flags(flags: List<String>): ItemStack {
        return buildItem(this) {
            this.flags.addAll(flags.map { ItemFlag.valueOf(it) })
        }
    }

    fun ItemStack.shiny(shiny: Boolean): ItemStack {
        return buildItem(this) {
            if (shiny) this.shiny()
        }
    }

    fun ItemStack.unbreakable(unbreakable: Boolean): ItemStack {
        return buildItem(this) {
            this.isUnbreakable = unbreakable
        }
    }

    fun ItemStack.originMeta(originMeta: ConfigurationSection): ItemStack {
        return buildItem(this) {
            when (this.originMeta) {
                is BannerMeta -> {
                    originMeta.getStringList("BannerPatterns").forEach {
                        val (colorValue, patternIdentifier) = it.split("-")
                        val color = DyeColor.valueOf(colorValue)
                        val pattern = PatternType.getByIdentifier(patternIdentifier) ?: return@forEach
                        this.patterns.add(Pattern(color, pattern))
                    }
                }

                is LeatherArmorMeta -> {
                    this.color = fromRGB(originMeta.getInt("Color"))
                }

                is PotionMeta -> {
                    originMeta.getString("BasePotionData.Type")?.let { potionTypeValue ->
                        potionData = PotionData(
                            PotionType.valueOf(potionTypeValue),
                            originMeta.getBoolean("BasePotionData.Extended"),
                            originMeta.getBoolean("BasePotionData.Upgraded")
                        )
                    }
                    originMeta.getStringList("PotionEffects").forEach { effect ->
                        val (typeName, amplifier, duration) = effect.split("-")
                        PotionEffectType.getByName(typeName)?.let {
                            potions.add(PotionEffect(it, duration.toInt(), amplifier.toInt()))
                        }
                    }
                }

                is SkullMeta -> {
                    this.skullOwner = originMeta.getString("SkullOwner")
                }
            }
        }
    }

    fun ItemStack.colored(): ItemStack {
        return buildItem(this) {
            this.colored()
        }
    }

    fun ItemStack.nbt(nbt: ConfigurationSection): ItemStack {
        val clone = buildItem(this)
        clone.itemTagReader {
            nbt.getKeys(true).forEach { set(it, nbt[it]) }
            write(clone)
        }
        return clone
    }
}