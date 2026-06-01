package xyz.crunchmunch.mods.gourmand.api

import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceKey
import xyz.crunchmunch.mods.gourmand.Gourmand
import xyz.crunchmunch.mods.gourmand.api.behavior.TriggerableEntityBehavior

object GourmandAttachments {
    @JvmField val BEHAVIORS: AttachmentType<List<ResourceKey<TriggerableEntityBehavior>>> = AttachmentRegistry.create(Gourmand.id("behavior")) { builder ->
        builder.persistent(ResourceKey.codec(GourmandRegistryKeys.BEHAVIOR).listOf())
            .syncWith(ByteBufCodecs.list<ByteBuf, ResourceKey<TriggerableEntityBehavior>>()
                .apply(ResourceKey.streamCodec(GourmandRegistryKeys.BEHAVIOR)), AttachmentSyncPredicate.all())
    }

    @JvmStatic
    fun init() {}
}
