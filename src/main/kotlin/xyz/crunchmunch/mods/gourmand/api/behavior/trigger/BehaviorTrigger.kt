package xyz.crunchmunch.mods.gourmand.api.behavior.trigger

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.predicates.entity.EntityPredicate
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import xyz.crunchmunch.mods.gourmand.api.GourmandRegistryKeys
import xyz.crunchmunch.mods.gourmand.workarounds.NullableNotNulls

@JvmRecord
data class BehaviorTrigger @JvmOverloads constructor(
    val type: ResourceKey<BehaviorTriggerType>,
    val conditions: Map<TriggerContext, EntityPredicate>,
    val serverOnly: Boolean = false,
) {
    fun canTrigger(interactingEntity: LivingEntity, interactedEntity: Entity): Boolean {
        val level = interactingEntity.level() as? ServerLevel

        if (this.serverOnly && level == null)
            return false

        if (this.conditions[TriggerContext.INTERACTING]?.matches(level, interactedEntity.position(), interactingEntity) == false)
            return false

        if (this.conditions[TriggerContext.INTERACTED]?.matches(level, interactedEntity.position(), interactedEntity) == false)
            return false

        return true
    }

    companion object {
        private fun EntityPredicate.matches(level: ServerLevel?, pos: Vec3, entity: Entity): Boolean {
            return if (level != null)
                this.matches(level, pos, entity)
            else
                NullableNotNulls.matches(this, pos, entity)
        }

        @JvmField val CODEC: Codec<BehaviorTrigger> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(GourmandRegistryKeys.BEHAVIOR_TRIGGER_TYPE)
                    .fieldOf("type")
                    .forGetter(BehaviorTrigger::type),
                Codec.unboundedMap(TriggerContext.CODEC, EntityPredicate.CODEC)
                    .optionalFieldOf("conditions", mapOf())
                    .forGetter(BehaviorTrigger::conditions),
                Codec.BOOL.optionalFieldOf("server_only", false)
                    .forGetter(BehaviorTrigger::serverOnly)
            )
                .apply(instance, ::BehaviorTrigger)
        }
    }
}
