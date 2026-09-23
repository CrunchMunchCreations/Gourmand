package xyz.crunchmunch.mods.gourmand.api.behavior.trigger

import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameType
import xyz.crunchmunch.mods.gourmand.Gourmand
import xyz.crunchmunch.mods.gourmand.api.GourmandAttachments
import xyz.crunchmunch.mods.gourmand.api.GourmandRegistryKeys

object BehaviorTriggerTypes {
    @JvmField val ENTITY_INSIDE = register("entity_inside") // collision
    @JvmField val ATTACK = register("attack") // left-click
    @JvmField val INTERACT = register("interact") // right-click
    @JvmField val JUMP_INSIDE = register("jump_inside") // jump when inside entity
    @JvmField val MOVE_INSIDE = register("move_inside") // move when inside entity

    private fun register(name: String): ResourceKey<BehaviorTriggerType> {
        return ResourceKey.create(GourmandRegistryKeys.BEHAVIOR_TRIGGER_TYPE, Gourmand.id(name))
    }

    @JvmStatic
    fun hasAnyTrigger(entity: Entity, vararg triggerIds: ResourceKey<BehaviorTriggerType>): Boolean {
        val behaviors = entity.getAttachedOrElse(GourmandAttachments.BEHAVIORS, emptyList())

        for (behaviorKey in behaviors) {
            val behavior = entity.registryAccess().getOrThrow(behaviorKey)
            if (triggerIds.contains(behavior.value().trigger.type)) {
                return true
            }
        }

        return false
    }

    @JvmStatic
    fun triggerBehaviors(interactedEntity: Entity, interactingEntity: LivingEntity, trigger: ResourceKey<BehaviorTriggerType>) {
        val behaviors = interactedEntity.getAttachedOrElse(GourmandAttachments.BEHAVIORS, emptyList())

        for (behaviorKey in behaviors) {
            val behavior = interactedEntity.registryAccess().getOrThrow(behaviorKey)

            if (interactingEntity is Player && !behavior.value().validGameTypes.contains(interactingEntity.gameMode() ?: GameType.ADVENTURE))
                continue

            if (behavior.value().trigger.type != trigger)
                continue

            if (behavior.value().trigger.canTrigger(interactingEntity, interactedEntity)) {
                behavior.value().behavior.handle(interactedEntity, interactingEntity)
            }
        }
    }
}
