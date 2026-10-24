package xyz.crunchmunch.mods.gourmand.api

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceKey
import xyz.crunchmunch.mods.gourmand.Gourmand
import xyz.crunchmunch.mods.gourmand.api.behavior.TriggerableEntityBehavior

object GourmandEntityAttachments {
    @JvmField val BEHAVIORS: AttachmentType<List<ResourceKey<TriggerableEntityBehavior>>> = AttachmentRegistry.create(Gourmand.id("behavior")) { builder ->
        builder.persistent(ResourceKey.codec(GourmandRegistryKeys.BEHAVIOR).listOf())
            .syncWith(ByteBufCodecs.list<ByteBuf, ResourceKey<TriggerableEntityBehavior>>()
                .apply(ResourceKey.streamCodec(GourmandRegistryKeys.BEHAVIOR)), AttachmentSyncPredicate.all())
    }

    @JvmField val MOD_IDS: AttachmentType<Map<String, String>> = AttachmentRegistry.create(Gourmand.id("mod_ids")) { builder ->
        builder.copyOnDeath()
    }

    /**
     * Entities with this attachment set to false will not collide with boats, and likewise boats will not collide with each other if this attachment is false.
     */
    @JvmField val ALLOW_BOAT_COLLISION: AttachmentType<Boolean> = AttachmentRegistry.create(Gourmand.id("allow_collision")) { builder ->
        builder.copyOnDeath()
            .persistent(Codec.BOOL)
            .syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
            .initializer { true }
    }

    @JvmStatic
    fun init() {}
}
