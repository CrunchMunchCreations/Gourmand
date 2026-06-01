package xyz.crunchmunch.mods.gourmand.api

import com.mojang.serialization.MapCodec
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.core.Registry
import xyz.crunchmunch.mods.gourmand.api.behavior.EntityBehavior

object GourmandRegistries {
    @JvmField val BEHAVIOR_TYPE: Registry<MapCodec<out EntityBehavior>> = FabricRegistryBuilder.create(GourmandRegistryKeys.BEHAVIOR_TYPE)
        .attribute(RegistryAttribute.SYNCED)
        .attribute(RegistryAttribute.OPTIONAL)
        .buildAndRegister()

    @JvmStatic
    fun init() {}
}
