package xyz.crunchmunch.mods.gourmand.api.behavior

import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameType
import xyz.crunchmunch.mods.gourmand.Gourmand
import xyz.crunchmunch.mods.gourmand.api.GourmandAttachments

object BehaviorTriggers {
    @JvmField val ENTITY_INSIDE = Gourmand.id("entity_inside") // collision
    @JvmField val ATTACK = Gourmand.id("attack") // left-click
    @JvmField val INTERACT = Gourmand.id("interact") // right-click
    @JvmField val JUMP_INSIDE = Gourmand.id("jump_inside") // jump when inside entity
    @JvmField val MOVE_INSIDE = Gourmand.id("move_inside") // move when inside entity

    @JvmStatic
    fun hasAnyTrigger(entity: Entity, vararg triggerIds: Identifier): Boolean {
        val behaviors = entity.getAttachedOrElse(GourmandAttachments.BEHAVIORS, emptyList())

        for (behaviorKey in behaviors) {
            val behavior = entity.registryAccess().getOrThrow(behaviorKey)
            if (triggerIds.contains(behavior.value().trigger)) {
                return true
            }
        }

        return false
    }

    @JvmStatic
    fun triggerBehaviors(interactedEntity: Entity, interactingEntity: LivingEntity, triggerId: Identifier) {
        val behaviors = interactedEntity.getAttachedOrElse(GourmandAttachments.BEHAVIORS, emptyList())

        for (behaviorKey in behaviors) {
            val behavior = interactedEntity.registryAccess().getOrThrow(behaviorKey)

            if (behavior.value().trigger == triggerId) {
                if (interactingEntity is Player && !behavior.value().validGameTypes.contains(interactingEntity.gameMode() ?: GameType.ADVENTURE))
                    continue

                behavior.value().behavior.handle(interactedEntity, interactingEntity)
            }
        }
    }
}
