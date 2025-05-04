package com.github.cpjinan.plugin.itemtools

import com.github.cpjinan.plugin.itemtools.ItemTools.plugin
import com.github.cpjinan.plugin.itemtools.utils.FileUtils.releaseResource
import org.bukkit.Color.fromRGB
import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
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
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XMaterial
import taboolib.module.configuration.Type
import taboolib.module.nms.itemTagReader
import taboolib.platform.util.buildItem
import taboolib.platform.util.giveItem
import top.maplex.arim.tools.folderreader.readFolderWalkConfig
import java.io.File

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/4 15:08
 */
object DefaultItemToolsManager : ItemToolsManager {
    override val item: HashMap<String, ItemStack> = hashMapOf()

    /** 重载物品配置 **/
    override fun reload() {
        readFolderWalkConfig(File("./plugins/ItemTools/item")) {
            setReadType(Type.YAML)
            walk {
                getKeys(false).forEach {
                    item[it] = buildItem(getConfigurationSection(it)!!)
                }
            }
        }
    }

    /** 给予玩家物品 **/
    override fun giveItem(player: Player, id: String, amount: Int) {
        player.giveItem(getItems()[id], amount)
    }

    /** 获取指定物品 **/
    override fun getItem(id: String): ItemStack? = getItems()[id]

    /** 获取物品列表 **/
    override fun getItems(): HashMap<String, ItemStack> = item

    /** 获取物品名称列表 **/
    override fun getItemNames(): List<String> = getItems().keys.toList()

    /** 从配置文件构建物品 **/
    override fun buildItem(config: ConfigurationSection): ItemStack {
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

    private fun material(material: String): ItemStack {
        return buildItem(XMaterial.valueOf(material))
    }

    private fun ItemStack.damage(damage: Int): ItemStack {
        return buildItem(this) {
            this.damage = damage
        }
    }

    private fun ItemStack.name(name: String): ItemStack {
        return buildItem(this) {
            this.name = name
        }
    }

    private fun ItemStack.lore(lore: List<String>): ItemStack {
        return buildItem(this) {
            this.lore.addAll(lore)
        }
    }

    private fun ItemStack.enchants(enchant: ConfigurationSection): ItemStack {
        return buildItem(this) {
            enchant.getKeys(false).forEach {
                val ench = Enchantment.getByName(it) ?: return@forEach
                this.enchants[ench] = enchant.getInt(it)
            }
        }
    }

    private fun ItemStack.flags(flags: List<String>): ItemStack {
        return buildItem(this) {
            this.flags.addAll(flags.map { ItemFlag.valueOf(it) })
        }
    }

    private fun ItemStack.shiny(shiny: Boolean): ItemStack {
        return buildItem(this) {
            if (shiny) this.shiny()
        }
    }

    private fun ItemStack.unbreakable(unbreakable: Boolean): ItemStack {
        return buildItem(this) {
            this.isUnbreakable = unbreakable
        }
    }

    private fun ItemStack.originMeta(originMeta: ConfigurationSection): ItemStack {
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

    private fun ItemStack.colored(): ItemStack {
        return buildItem(this) {
            this.colored()
        }
    }

    private fun ItemStack.nbt(nbt: ConfigurationSection): ItemStack {
        val clone = buildItem(this)
        clone.itemTagReader {
            nbt.getKeys(true).forEach { set(it, nbt[it]) }
            write(clone)
        }
        return clone
    }

    @Awake(LifeCycle.CONST)
    fun onConst() {
        PlatformFactory.registerAPI<ItemToolsManager>(DefaultItemToolsManager)
    }

    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        plugin.releaseResource("item/Example.yml")
        reload()
    }
}