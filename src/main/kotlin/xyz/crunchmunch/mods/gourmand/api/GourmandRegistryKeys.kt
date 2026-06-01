package xyz.crunchmunch.mods.gourmand.api

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import xyz.crunchmunch.mods.gourmand.Gourmand
import xyz.crunchmunch.mods.gourmand.api.behavior.EntityBehavior
import xyz.crunchmunch.mods.gourmand.api.behavior.TriggerableEntityBehavior

object GourmandRegistryKeys {
    // Built-in
    @JvmField val BEHAVIOR_TYPE = key<MapCodec<out EntityBehavior>>("behavior_type")

    // Data-driven
    @JvmField val BEHAVIOR = key<TriggerableEntityBehavior>("behavior")

    private fun <T : Any> key(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(Gourmand.id(path))
}
