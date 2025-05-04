package com.github.cpjinan.plugin.itemtools

import com.github.cpjinan.plugin.itemtools.hook.MythicMobsHook

/**
 * ItemTools
 * com.github.cpjinan.plugin.itemtools
 *
 * @author 季楠
 * @since 2025/5/1 17:05
 */
interface ItemToolsHook {
    fun getMythicMobs(): MythicMobsHook
}