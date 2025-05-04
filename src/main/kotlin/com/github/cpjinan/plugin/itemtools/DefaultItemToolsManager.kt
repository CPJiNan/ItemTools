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
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName
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
                    item[it] = getItemFromConfig(getConfigurationSection(it)!!)
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

    // region getItemFromConfig

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

    // endregion

    /** 保存物品到配置文件 **/
    override fun saveItemToConfig(item: ItemStack, config: Configuration, id: String) {
        material(item, config, id)
        damage(item, config, id)
        name(item, config, id)
        lore(item, config, id)
        enchants(item, config, id)
        flags(item, config, id)
        unbreakable(item, config, id)
        originMeta(item, config, id)
        nbt(item, config, id)
    }

    // region saveItemToConfig

    private fun material(item: ItemStack, config: Configuration, path: String) {
        buildItem(item) {
            config["$path.Type"] = XMaterial.matchXMaterial(this.material).name
        }
    }

    private fun damage(item: ItemStack, config: Configuration, path: String) {
        buildItem(item) {
            if (this.damage == 0) return@buildItem
            config["$path.Data"] = this.damage
        }
    }

    private fun name(item: ItemStack, config: Configuration, path: String) {
        buildItem(item) {
            config["$path.Display"] = item.getName()
        }
    }

    private fun lore(item: ItemStack, config: Configuration, path: String) {
        buildItem(item) {
            if (this.lore.isEmpty()) return@buildItem
            config["$path.Lore"] = this.lore
        }
    }

    private fun enchants(item: ItemStack, config: Configuration, path: String) {
        buildItem(item) {
            if (this.enchants.isEmpty()) return@buildItem
            this.enchants.forEach { (ench, level) ->
                config["$path.Enchantments.${ench.name}"] = level
            }
            config["$path.Options.Glow"] = true
        }
    }

    private fun flags(item: ItemStack, config: Configuration, path: String) {
        buildItem(item) {
            if (this.flags.isEmpty()) return@buildItem
            config["$path.Options.HideFlags"] = this.flags.map { it.name }
        }
    }

    private fun unbreakable(item: ItemStack, config: Configuration, path: String) {
        buildItem(item) {
            if (this.isUnbreakable) config["$path.Options.Unbreakable"] = true
        }
    }

    private fun originMeta(item: ItemStack, config: Configuration, path: String) {
        buildItem(item) {
            when (this.originMeta) {
                is BannerMeta -> {
                    config["$path.Options.BannerPatterns"] =
                        this.patterns.map { "${it.color.name}-${it.pattern.identifier}" }
                }

                is LeatherArmorMeta -> {
                    if (this.color == null) return@buildItem
                    config["$path.Options.Color"] = this.color!!.asRGB()
                }

                is PotionMeta -> {
                    if (this.potionData == null) return@buildItem
                    config["$path.Options.BasePotionData.Type"] = this.potionData!!.type.name
                    config["$path.Options.BasePotionData.Extended"] = this.potionData!!.isExtended
                    config["$path.Options.BasePotionData.Upgraded"] = this.potionData!!.isUpgraded
                    config["$path.Options.PotionEffects"] =
                        this.potions.map { "${it.type.name}-${it.amplifier}-${it.duration}" }
                }

                is SkullMeta -> {
                    if (skullOwner == null) return@buildItem
                    config["$path.Options.SkullOwner"] = skullOwner
                }
            }
        }
    }

    private fun nbt(item: ItemStack, config: Configuration, path: String) {
        item.getItemTag().entries.filter {
            it.key !in listOf(
                "display", "Damage", "ench", "Enchantments",
                "Unbreakable", "HideFlags",
                "BlockEntityTag", "Potion", "SkullOwner"
            )
        }.forEach {
            config["$path.NBT.${it.key}"] = it.value.unsafeData()
        }
    }

    // endregion

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