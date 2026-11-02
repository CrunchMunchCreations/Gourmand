package xyz.crunchmunch.mods.gourmand.api.debug

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3
import xyz.crunchmunch.mods.gourmand.network.listOf

@JvmRecord
data class DebugPath(
    val points: List<Vec3>,
    val color: Int
) {
    companion object {
        @JvmField val STREAM_CODEC: StreamCodec<ByteBuf, DebugPath> = StreamCodec.composite(
            Vec3.STREAM_CODEC.listOf(), DebugPath::points,
            ByteBufCodecs.INT, DebugPath::color,
            ::DebugPath
        )
    }
}
