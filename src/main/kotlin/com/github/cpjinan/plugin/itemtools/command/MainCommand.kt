package com.github.cpjinan.plugin.itemtools.command

import com.github.cpjinan.plugin.itemtools.ItemTools
import com.github.cpjinan.plugin.itemtools.ItemToolsSettings
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.module.lang.sendLang
import top.maplex.arim.tools.commandhelper.createTabooLegacyStyleCommandHelper

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.command
 *
 * @author 季楠
 * @since 2025/5/4 00:14
 */
@CommandHeader(
    name = "itemtools",
    aliases = ["its"],
    permission = "ItemTools.command.use",
    permissionDefault = PermissionDefault.OP
)
object MainCommand {
    @CommandBody
    val main = mainCommand {
        createTabooLegacyStyleCommandHelper()
    }

    @CommandBody(
        permission = "ItemTools.command.item.use",
        permissionDefault = PermissionDefault.OP
    )
    val item = subCommand {
        execute<ProxyCommandSender> { sender, _, content ->
            dynamic {
                sender.performCommand("itemtools:$content")
            }
        }
    }

    @CommandBody(
        aliases = ["lore"],
        permission = "ItemTools.command.loreedit.use",
        permissionDefault = PermissionDefault.OP
    )
    val loreedit = subCommand {
        execute<ProxyCommandSender> { sender, _, content ->
            dynamic {
                sender.performCommand("itemtools:$content")
            }
        }
    }

    @CommandBody(
        aliases = ["nbt"],
        permission = "ItemTools.command.nbtedit.use",
        permissionDefault = PermissionDefault.OP
    )
    val nbtedit = subCommand {
        execute<ProxyCommandSender> { sender, _, content ->
            dynamic {
                sender.performCommand("itemtools:$content")
            }
        }
    }

    @CommandBody(
        permission = "ItemTools.command.unbreakable.use",
        permissionDefault = PermissionDefault.OP
    )
    val unbreakable = subCommand {
        execute<ProxyCommandSender> { sender, _, content ->
            dynamic {
                sender.performCommand("itemtools:$content")
            }
        }
    }

    @CommandBody(
        aliases = ["ench"],
        permission = "ItemTools.command.enchant.use",
        permissionDefault = PermissionDefault.OP
    )
    val enchant = subCommand {
        execute<ProxyCommandSender> { sender, _, content ->
            dynamic {
                sender.performCommand("itemtools:$content")
            }
        }
    }

    @CommandBody(permission = "ItemTools.command.reload.use", permissionDefault = PermissionDefault.OP)
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            ItemToolsSettings.settings.reload()
            ItemTools.api().getLanguage().reload()
            ItemTools.api().getManager().reload()
            sender.sendLang("Plugin-Reloaded")
        }
    }
}