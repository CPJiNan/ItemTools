package com.github.cpjinan.plugin.itemtools.command.subcommand

import com.github.cpjinan.plugin.itemtools.ItemTools
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.util.isConsole
import taboolib.module.lang.sendLang
import top.maplex.arim.tools.commandhelper.createTabooLegacyStyleCommandHelper

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.command.subcommand
 *
 * @author 季楠
 * @since 2025/5/3 16:47
 */
@CommandHeader(
    name = "unbreakable",
    aliases = ["itsunbreakable"],
    permission = "ItemTools.command.unbreakable.use",
    permissionDefault = PermissionDefault.OP
)
object UnbreakableCommand {
    val serviceAPI = ItemTools.api().getService()

    @Suppress("DEPRECATION")
    @CommandBody
    val main = mainCommand {
        createTabooLegacyStyleCommandHelper("unbreakable")
        bool("bool") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                setUnbreakable(sender, sender.cast<Player>().itemInHand, context.bool("bool"))
            }
        }
    }

    /** 设置物品无法破坏 **/
    fun setUnbreakable(sender: ProxyCommandSender, item: ItemStack) {
        if (serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        val bool = !serviceAPI.isUnbreakable(item)
        serviceAPI.setUnbreakable(item, bool)

        if (bool) sender.sendLang("Unbreakable-True")
        else sender.sendLang("Unbreakable-False")
    }

    /** 设置物品无法破坏 **/
    fun setUnbreakable(sender: ProxyCommandSender, item: ItemStack, bool: Boolean) {
        if (serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        serviceAPI.setUnbreakable(item, bool)

        if (bool) sender.sendLang("Unbreakable-True")
        else sender.sendLang("Unbreakable-False")
    }
}