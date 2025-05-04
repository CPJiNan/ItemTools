package com.github.cpjinan.plugin.itemtools.command.subcommand

import com.github.cpjinan.plugin.itemtools.ItemTools
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.module.lang.sendLang
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

    /** 查看物品列表 **/
    fun listItem(sender: ProxyCommandSender) {
        sender.sendMessage("")
        sender.sendLang("Item-List")
        sender.sendMessage("")
        sender.sendMessage(managerAPI.item.keys.joinToString("§7, ") { "§f$it" })
        sender.sendMessage("")
    }
}