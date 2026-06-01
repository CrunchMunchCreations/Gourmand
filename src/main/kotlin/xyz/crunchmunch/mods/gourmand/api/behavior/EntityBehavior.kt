package xyz.crunchmunch.mods.gourmand.api.behavior

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import xyz.crunchmunch.mods.gourmand.api.GourmandRegistries

interface EntityBehavior {
    fun handle(interactedEntity: Entity, interactingEntity: LivingEntity)
    val codec: MapCodec<out EntityBehavior>

    companion object {
        @JvmField val CODEC: Codec<EntityBehavior> = GourmandRegistries.BEHAVIOR_TYPE.byNameCodec()
            .dispatch("type", EntityBehavior::codec) { it }
    }
}
