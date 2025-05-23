package com.github.cpjinan.plugin.itemtools.command.subcommand

import com.github.cpjinan.plugin.itemtools.ItemTools
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.util.isConsole
import taboolib.module.lang.sendLang
import taboolib.module.nms.*
import top.maplex.arim.tools.commandhelper.createTabooLegacyStyleCommandHelper

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.command.subcommand
 *
 * @author 季楠
 * @since 2025/5/2 11:17
 */
@CommandHeader(
    name = "nbtedit",
    aliases = ["nbt"],
    permission = "ItemTools.command.nbtedit.use",
    permissionDefault = PermissionDefault.OP
)
object NBTCommand {
    val serviceAPI = ItemTools.api().getService()

    @CommandBody
    val main = mainCommand {
        createTabooLegacyStyleCommandHelper("nbtedit")
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.nbtedit.check", permissionDefault = PermissionDefault.OP)
    val check = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            if (sender.isConsole()) {
                sender.sendLang("Error-Not-Player")
                return@execute
            }

            checkNBT(sender, sender.cast<Player>().itemInHand)
        }
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.nbtedit.set", permissionDefault = PermissionDefault.OP)
    val set = subCommand {
        dynamic("key").dynamic("value") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                setNBT(sender, sender.cast<Player>().itemInHand, context["key"], context["value"])
            }
        }
    }

    /** 查看物品 NBT **/
    fun checkNBT(sender: ProxyCommandSender, item: ItemStack) {
        if (serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        fun getItemTag(tag: ItemTag, indent: String): List<String> {
            val itemTag = mutableListOf<String>()
            tag.forEach { (key, value) ->
                when (value) {
                    is ItemTag -> {
                        itemTag.add("$indent§7$key§8:")
                        itemTag.addAll(getItemTag(value, "$indent  "))
                    }

                    is ItemTagList -> {
                        itemTag.add("$indent§7$key§8:")
                        value.forEach {
                            when (it.type) {
                                ItemTagType.COMPOUND -> itemTag.addAll(getItemTag(it.asCompound(), "$indent  "))
                                else -> itemTag.add("$indent  §f- §f$it§f")
                            }
                        }
                    }

                    else -> itemTag.add("$indent§7$key§8: §f$value§f")
                }
            }
            return itemTag
        }

        sender.sendMessage("")
        sender.sendLang("NBT-Check", item.getName())
        sender.sendMessage("")
        getItemTag(item.getItemTag(), "  ").forEach {
            sender.sendMessage(it)
        }
        sender.sendMessage("")
    }

    /** 设置物品 NBT **/
    fun setNBT(sender: ProxyCommandSender, item: ItemStack, key: String, value: String) {
        if (serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        serviceAPI.setNBT(item, key, value)

        sender.sendLang("NBT-Set", key, value)
    }
}