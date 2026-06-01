package xyz.crunchmunch.mods.gourmand.network.clientbound

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import xyz.crunchmunch.mods.gourmand.Gourmand

@JvmRecord
data class IgnoredBlockUpdateListPacket(
    val states: List<BlockState>
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<IgnoredBlockUpdateListPacket> = CustomPacketPayload.Type(Gourmand.id("ignored_block_update_list"))
        val CODEC: StreamCodec<ByteBuf, IgnoredBlockUpdateListPacket> = StreamCodec.composite(
            ByteBufCodecs.list<ByteBuf, BlockState>().apply(
                ByteBufCodecs.VAR_INT.map({ if (it == 0) Blocks.AIR.defaultBlockState() else Block.stateById(it) }, { Block.getId(it) })
            ), IgnoredBlockUpdateListPacket::states,
            ::IgnoredBlockUpdateListPacket
        )
    }
}
