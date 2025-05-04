package com.github.cpjinan.plugin.itemtools.command.subcommand

import com.github.cpjinan.plugin.itemtools.ItemTools
import com.github.cpjinan.plugin.itemtools.utils.FileUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.util.isConsole
import taboolib.module.chat.uncolored
import taboolib.module.configuration.Configuration
import taboolib.module.lang.sendLang
import taboolib.module.nms.getName
import top.maplex.arim.tools.commandhelper.createTabooLegacyStyleCommandHelper

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.command.subcommand
 *
 * @author 季楠
 * @since 2025/5/4 16:45
 */
@CommandHeader(
    name = "item",
    permission = "ItemTools.command.item.use",
    permissionDefault = PermissionDefault.OP
)
object ItemCommand {
    val managerAPI = ItemTools.api().getManager()

    @CommandBody
    val main = mainCommand {
        createTabooLegacyStyleCommandHelper("item")
    }

    @CommandBody(permission = "ItemTools.command.item.list", permissionDefault = PermissionDefault.OP)
    val list = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            listItem(sender)
        }
    }

    @CommandBody(permission = "ItemTools.command.item.get", permissionDefault = PermissionDefault.OP)
    val get = subCommand {
        dynamic("id") {
            suggestUncheck { managerAPI.getItemNames() }
            execute<ProxyCommandSender> { sender, context, _ ->
                getItem(sender, context["id"])
            }
        }.int("amount") {
            execute<ProxyCommandSender> { sender, context, _ ->
                getItem(sender, context["id"], context.int("amount"))
            }
        }
    }

    @CommandBody(permission = "ItemTools.command.item.give", permissionDefault = PermissionDefault.OP)
    val give = subCommand {
        player("player") { suggestPlayers() }.dynamic("id") {
            suggestUncheck { managerAPI.getItemNames() }
            execute<ProxyCommandSender> { sender, context, _ ->
                giveItem(sender, context.player("player").cast(), context["id"])
            }
        }.int("amount") {
            execute<ProxyCommandSender> { sender, context, _ ->
                giveItem(sender, context.player("player").cast(), context["id"], context.int("amount"))
            }
        }
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.item.save", permissionDefault = PermissionDefault.OP)
    val save = subCommand {
        execute<ProxyCommandSender> { sender, context, _ ->
            if (sender.isConsole()) {
                sender.sendLang("Error-Not-Player")
                return@execute
            }
            val item = sender.cast<Player>().itemInHand
            saveItem(sender, item, item.getName().uncolored())
        }
        dynamic("id") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }
                saveItem(sender, sender.cast<Player>().itemInHand, context["id"])
            }
        }.dynamic("path") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }
                saveItem(sender, sender.cast<Player>().itemInHand, context["id"], context["path"])
            }
        }
    }

    /** 查看物品列表 **/
    fun listItem(sender: ProxyCommandSender) {
        sender.sendMessage("")
        sender.sendLang("Item-List")
        sender.sendMessage("")
        sender.sendMessage(managerAPI.getItemNames().joinToString("§7, ") { "§f$it" })
        sender.sendMessage("")
    }

    /** 获取物品 **/
    fun getItem(sender: ProxyCommandSender, id: String, amount: Int = 1) {
        if (sender.isConsole()) {
            sender.sendLang("Error-Not-Player")
            return
        }

        if (id !in managerAPI.item) {
            sender.sendLang("Item-Not-Found", id)
            return
        }

        managerAPI.giveItem(sender.cast(), id, amount)

        sender.sendLang("Item-Get", id, amount)
    }

    /** 给予物品 **/
    fun giveItem(sender: ProxyCommandSender, player: Player, id: String, amount: Int = 1) {
        if (id !in managerAPI.item) {
            sender.sendLang("Item-Not-Found", id)
            return
        }

        managerAPI.giveItem(player, id, amount)

        sender.sendLang("Item-Give", id, amount, player.name)
    }

    /** 保存物品 **/
    fun saveItem(sender: ProxyCommandSender, item: ItemStack, id: String, path: String = "SaveItem.yml") {
        val savePath = "item/${path.takeIf { it.endsWith(".yml") } ?: "$path.yml"}"
        val file = FileUtils.getFileOrCreate(savePath)
        val config = Configuration.loadFromFile(file)
        config[id] = null
        managerAPI.saveItemToConfig(item, config, id)
        config.saveToFile(file)
        managerAPI.reload()
        sender.sendLang("Item-Save", path, id)
    }
}