package com.github.cpjinan.plugin.itemtools.listener

import com.github.cpjinan.plugin.itemtools.ItemToolsSettings
import com.github.cpjinan.plugin.itemtools.network.ItemToolsUpdate
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools.listener
 *
 * @author 季楠
 * @since 2025/5/4 23:34
 */
object PlayerListener {
    @SubscribeEvent
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.isOp && ItemToolsSettings.checkUpdate && ItemToolsSettings.opNotify) {
            ItemToolsUpdate.sendPlayerUpdate(event.player)
        }
        if (event.player.isOp) ItemToolsUpdate.sendPlayerNotice(event.player)
    }
}