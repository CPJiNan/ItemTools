package com.github.cpjinan.plugin.itemtools.command

import com.github.cpjinan.plugin.itemtools.ItemTools
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.util.isConsole
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.command
 *
 * @author 季楠
 * @since 2025/5/3 16:47
 */
@CommandHeader(
    name = "unbreakable",
    permission = "ItemTools.command.unbreakable.use",
    permissionDefault = PermissionDefault.OP
)
object UnbreakableCommand {
    val serviceAPI = ItemTools.api().getService()

    @Suppress("DEPRECATION")
    @CommandBody
    val main = mainCommand {
        createHelper()
        bool("isUnbreakable") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                setUnbreakable(sender, sender.cast<Player>().itemInHand, context.bool("isUnbreakable"))
            }
        }
    }

    /** 设置物品无法破坏 **/
    fun setUnbreakable(sender: ProxyCommandSender, item: ItemStack, isUnbreakable: Boolean) {
        if (serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        serviceAPI.setUnbreakable(item, isUnbreakable)

        if (isUnbreakable) sender.sendLang("Unbreakable-True")
        else sender.sendLang("Unbreakable-False")
    }
}