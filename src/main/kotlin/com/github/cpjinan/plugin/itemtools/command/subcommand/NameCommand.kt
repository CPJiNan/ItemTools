package com.github.cpjinan.plugin.itemtools.command.subcommand

import com.github.cpjinan.plugin.itemtools.ItemTools
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.util.isConsole
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import top.maplex.arim.tools.commandhelper.createTabooLegacyStyleCommandHelper

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.command.subcommand
 *
 * @author 季楠
 * @since 2025/5/8 21:52
 */
@CommandHeader(
    name = "nameedit",
    permission = "ItemTools.command.nameedit.use",
    permissionDefault = PermissionDefault.OP
)
object NameCommand {
    val serviceAPI = ItemTools.api().getService()

    @CommandBody
    val main = mainCommand {
        createTabooLegacyStyleCommandHelper("nameedit")
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.nameedit.set", permissionDefault = PermissionDefault.OP)
    val set = subCommand {
        dynamic("name") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                setName(sender, sender.cast<Player>().itemInHand, context["name"].colored())
            }
        }
    }

    /** 设置物品名称 **/
    fun setName(sender: ProxyCommandSender, item: ItemStack, name: String) {
        if (serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        serviceAPI.setName(item, name)

        sender.sendLang("Name-Set", name)
    }
}