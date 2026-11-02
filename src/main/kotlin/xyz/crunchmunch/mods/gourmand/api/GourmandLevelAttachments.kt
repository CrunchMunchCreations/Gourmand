package xyz.crunchmunch.mods.gourmand.api

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.commands.Commands
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.Identifier
import xyz.crunchmunch.mods.gourmand.Gourmand
import xyz.crunchmunch.mods.gourmand.api.debug.DebugPath

object GourmandLevelAttachments {
    @JvmField val DEBUG_PATHS: AttachmentType<Map<Identifier, DebugPath>> = AttachmentRegistry.create(Gourmand.id("debug_paths")) { builder ->
        builder.syncWith(ByteBufCodecs.map(::HashMap, Identifier.STREAM_CODEC, DebugPath.STREAM_CODEC)) { _, player ->
            player.checkPermission(GourmandPermissions.VISUALIZE_DEBUG_PATHS)
                ?: Commands.LEVEL_GAMEMASTERS.check(player.permissions())
        }
            .initializer { mapOf() }
    }

    fun init() {}
}
