package xyz.crunchmunch.mods.gourmand.api.impl.behavior

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import xyz.crunchmunch.mods.gourmand.Gourmand
import xyz.crunchmunch.mods.gourmand.api.GourmandRegistries
import xyz.crunchmunch.mods.gourmand.api.behavior.EntityBehavior

object GourmandBehaviorTypes {
    @JvmField val ENTITY_EFFECT = register("entity_effect", EntityEffectBehavior.CODEC)
    @JvmField val ENTITY_VELOCITY = register("entity_velocity", EntityVelocityBehavior.CODEC)

    private fun <T : EntityBehavior> register(name: String, codec: MapCodec<T>): MapCodec<T> {
        return Registry.register(GourmandRegistries.BEHAVIOR_TYPE, Gourmand.id(name), codec)
    }

    fun init() {}
}
