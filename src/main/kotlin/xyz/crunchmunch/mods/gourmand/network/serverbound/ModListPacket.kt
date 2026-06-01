package xyz.crunchmunch.mods.gourmand.network.serverbound

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import xyz.crunchmunch.mods.gourmand.Gourmand

@JvmRecord
data class ModListPacket(
    val modIdsToVersions: Map<String, String>
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<ModListPacket> = CustomPacketPayload.Type(Gourmand.id("mod_list"))
        val CODEC: StreamCodec<FriendlyByteBuf, ModListPacket> = StreamCodec.composite(
            ByteBufCodecs.map(::HashMap, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.STRING_UTF8), ModListPacket::modIdsToVersions,
            ::ModListPacket
        )
    }
}
