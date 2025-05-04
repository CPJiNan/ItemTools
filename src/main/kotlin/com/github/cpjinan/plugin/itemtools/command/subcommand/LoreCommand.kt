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

    /** 查看物品 Lore **/
    fun checkLore(sender: ProxyCommandSender, item: ItemStack) {
        if (NBTCommand.serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        sender.sendLang("Lore-Check", item.getName())
        serviceAPI.getLore(item).forEachIndexed { index, element ->
            sender.sendMessage("&7${index + 1} &8| &r$element".colored())
        }
    }
}