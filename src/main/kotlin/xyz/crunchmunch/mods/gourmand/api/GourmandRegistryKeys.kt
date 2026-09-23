package xyz.crunchmunch.mods.gourmand.api

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import xyz.crunchmunch.mods.gourmand.Gourmand
import xyz.crunchmunch.mods.gourmand.api.behavior.EntityBehavior
import xyz.crunchmunch.mods.gourmand.api.behavior.TriggerableEntityBehavior
import xyz.crunchmunch.mods.gourmand.api.behavior.trigger.BehaviorTriggerType

object GourmandRegistryKeys {
    // Built-in
    @JvmField val BEHAVIOR_TYPE = key<MapCodec<out EntityBehavior>>("behavior_type")
    @JvmField val BEHAVIOR_TRIGGER_TYPE = key<BehaviorTriggerType>("behavior_trigger_type")

    // Data-driven
    @JvmField val BEHAVIOR = key<TriggerableEntityBehavior>("behavior")

    private fun <T : Any> key(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(Gourmand.id(path))
}
