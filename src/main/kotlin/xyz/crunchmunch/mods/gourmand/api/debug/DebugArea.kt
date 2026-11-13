package xyz.crunchmunch.mods.gourmand.api.debug

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.AABB
import xyz.crunchmunch.mods.gourmand.api.GourmandStreamCodecs

@JvmRecord
data class DebugArea(
    val area: AABB,
    val color: Int,
) {
    companion object {
        @JvmField val STREAM_CODEC: StreamCodec<ByteBuf, DebugArea> = StreamCodec.composite(
            GourmandStreamCodecs.BOUNDING_BOX, DebugArea::area,
            ByteBufCodecs.VAR_INT, DebugArea::color,
            ::DebugArea
        )
    }
}
