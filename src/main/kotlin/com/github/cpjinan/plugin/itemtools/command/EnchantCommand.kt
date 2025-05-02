package com.github.cpjinan.plugin.itemtools.command

import com.github.cpjinan.plugin.itemtools.ItemTools
import org.bukkit.enchantments.Enchantment
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
 * @since 2025/5/2 11:17
 */
@CommandHeader(
    name = "enchant",
    aliases = ["ench"],
    permission = "ItemTools.command.enchant.use",
    permissionDefault = PermissionDefault.OP
)
object EnchantCommand {
    val serviceAPI = ItemTools.api().getService()

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.enchant.check", permissionDefault = PermissionDefault.OP)
    val check = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            if (sender.isConsole()) {
                sender.sendLang("Error-Not-Player")
                return@execute
            }

            checkEnchant(sender, sender.cast<Player>().itemInHand)
        }
    }

    @Suppress("DEPRECATION")
    @CommandBody(permission = "ItemTools.command.enchant.set", permissionDefault = PermissionDefault.OP)
    val set = subCommand {
        dynamic("enchant").int("level") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isConsole()) {
                    sender.sendLang("Error-Not-Player")
                    return@execute
                }

                setEnchant(sender, sender.cast<Player>().itemInHand, context["enchant"], context.int("level"))
            }
        }
    }

    /** 查看物品附魔 **/
    fun checkEnchant(sender: ProxyCommandSender, item: ItemStack) {
        if (serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        sender.sendLang("Enchant-Check")
        serviceAPI.getEnchant(item).forEach { (ench, level) ->
            sender.sendMessage("  §7${ench}§8: §f$level")
        }
    }

    /** 设置物品附魔 **/
    fun setEnchant(sender: ProxyCommandSender, item: ItemStack, ench: String, level: Int) {
        if (serviceAPI.isAir(item)) {
            sender.sendLang("Error-Air-In-Hand")
            return
        }

        val enchant = Enchantment.getByName(ench)
        if (enchant == null) {
            sender.sendLang("Enchant-Not-Found", ench)
            return
        }

        serviceAPI.setEnchant(item, ench, level)

        sender.sendLang("Enchant-Set", ench, level)
    }
}