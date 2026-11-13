package xyz.crunchmunch.mods.gourmand.api

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object GourmandStreamCodecs {
    @JvmField val BOUNDING_BOX: StreamCodec<ByteBuf, AABB> = StreamCodec.composite(
        Vec3.STREAM_CODEC, AABB::getMinPosition,
        Vec3.STREAM_CODEC, AABB::getMaxPosition,
        ::AABB
    )
}
