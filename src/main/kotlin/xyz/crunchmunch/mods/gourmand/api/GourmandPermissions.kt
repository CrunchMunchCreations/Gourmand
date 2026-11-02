package xyz.crunchmunch.mods.gourmand.api

import net.fabricmc.fabric.api.permission.v1.PermissionNode
import xyz.crunchmunch.mods.gourmand.Gourmand

object GourmandPermissions {
    @JvmField val VISUALIZE_DEBUG_PATHS = PermissionNode.of(Gourmand.id("visualize_debug_paths"))
}
