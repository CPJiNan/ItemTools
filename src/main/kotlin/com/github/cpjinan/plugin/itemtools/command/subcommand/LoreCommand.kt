package com.github.cpjinan.plugin.itemtools.command.subcommand

import com.github.cpjinan.plugin.itemtools.ItemTools
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.util.isConsole
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.nms.getName
import top.maplex.arim.tools.commandhelper.createTabooLegacyStyleCommandHelper

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.command.subcommand
 *
 * @author 季楠
 * @since 2025/5/4 10:28
 */
@CommandHeader(
    name = "loreedit",
    aliases = ["lore"],
    permission = "ItemTools.command.loreedit.use",
    permissionDefault = PermissionDefault.OP
)
object LoreCommand {
    val serviceAPI = ItemTools.api().getService()

    @CommandBody
    val main = mainCommand {
        createTabooLegacyStyleCommandHelper("loreedit")
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.loreedit.check", permissionDefault = PermissionDefault.OP)
    val check = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            if (sender.isConsole()) {
                sender.sendLang("Error-Not-Player")
                return@execute
            }

            checkLore(sender, sender.cast<Player>().itemInHand)
        }
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.loreedit.add", permissionDefault = PermissionDefault.OP)
    val add = subCommand {
        dynamic("element") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                addLore(sender, sender.cast<Player>().itemInHand, context["element"].colored())
            }
        }
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.loreedit.remove", permissionDefault = PermissionDefault.OP)
    val remove = subCommand {
        int("index") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                removeLore(sender, sender.cast<Player>().itemInHand, context.int("index"))
            }
        }
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.loreedit.set", permissionDefault = PermissionDefault.OP)
    val set = subCommand {
        int("index").dynamic("element") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                setLore(sender, sender.cast<Player>().itemInHand, context.int("index"), context["element"].colored())
            }
        }
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.loreedit.insert", permissionDefault = PermissionDefault.OP)
    val insert = subCommand {
        int("index").dynamic("element") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                insertLore(sender, sender.cast<Player>().itemInHand, context.int("index"), context["element"].colored())
            }
        }
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.loreedit.clear", permissionDefault = PermissionDefault.OP)
    val clear = subCommand {
        execute<ProxyCommandSender> { sender, context, _ ->
            if (sender.isConsole()) {
                sender.sendLang("Error-Not-Player")
                return@execute
            }

            clearLore(sender, sender.cast<Player>().itemInHand)
        }
    }

    /** 查看物品 Lore **/
    fun checkLore(sender: ProxyCommandSender, item: ItemStack) {
        if (NBTCommand.serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        sender.sendMessage("")
        sender.sendLang("Lore-Check", item.getName())
        sender.sendMessage("")
        serviceAPI.getLore(item).forEachIndexed { index, element ->
            sender.sendMessage("&7${index + 1} &8| &r$element".colored())
        }
        sender.sendMessage("")
    }

    /** 新增物品 Lore **/
    fun addLore(sender: ProxyCommandSender, item: ItemStack, element: String) {
        if (NBTCommand.serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        serviceAPI.addLore(item, element)
        sender.sendLang("Lore-Add", element)
    }

    /** 移除物品 Lore **/
    fun removeLore(sender: ProxyCommandSender, item: ItemStack, index: Int) {
        if (NBTCommand.serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        if (index !in 1..serviceAPI.getLore(item).size) {
            sender.sendLang("Lore-Index-Not-Found", index, serviceAPI.getLore(item).size)
            return
        }

        serviceAPI.removeLore(item, index - 1)
        sender.sendLang("Lore-Remove", index)
    }

    /** 设置物品 Lore **/
    fun setLore(sender: ProxyCommandSender, item: ItemStack, index: Int, element: String) {
        if (NBTCommand.serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        if (index !in 1..serviceAPI.getLore(item).size) {
            sender.sendLang("Lore-Index-Not-Found", index, serviceAPI.getLore(item).size)
            return
        }

        serviceAPI.setLore(item, index - 1, element)
        sender.sendLang("Lore-Set", index, element)
    }

    /** 插入物品 Lore **/
    fun insertLore(sender: ProxyCommandSender, item: ItemStack, index: Int, element: String) {
        if (NBTCommand.serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        if (index !in 1..serviceAPI.getLore(item).size) {
            sender.sendLang("Lore-Index-Not-Found", index, serviceAPI.getLore(item).size)
            return
        }

        serviceAPI.addLore(item, index, element)
        sender.sendLang("Lore-Insert", index, index + 1, element)
    }

    /** 清空物品 Lore **/
    fun clearLore(sender: ProxyCommandSender, item: ItemStack) {
        if (NBTCommand.serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        serviceAPI.removeLore(item)
        sender.sendLang("Lore-Clear")
    }
}