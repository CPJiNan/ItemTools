package com.github.cpjinan.plugin.itemtools.command.subcommand

import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.PermissionDefault

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
}