package xyz.crunchmunch.mods.gourmand.network

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

fun <B : ByteBuf, V : Any> StreamCodec<B, V>.listOf(): StreamCodec<B, List<V>>
    = ByteBufCodecs.list<B, V>().apply(this)

fun <B : ByteBuf, V : Any> StreamCodec<B, V>.listOf(size: Int): StreamCodec<B, List<V>>
    = ByteBufCodecs.list<B, V>(size).apply(this)

fun <B : ByteBuf, V : Any> StreamCodec<B, V>.optional(): StreamCodec<B, Optional<V>>
    = ByteBufCodecs.optional(this)
