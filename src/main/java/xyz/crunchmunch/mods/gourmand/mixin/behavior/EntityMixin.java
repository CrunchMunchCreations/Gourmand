package xyz.crunchmunch.mods.gourmand.mixin.behavior;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.crunchmunch.mods.gourmand.api.behavior.BehaviorTriggers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;

@Mixin(Entity.class)
public abstract class EntityMixin implements AttachmentTarget {
    @Shadow public abstract AABB getBoundingBox();
    @Shadow public abstract Level level();

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void checkCollidingEntities(CallbackInfo ci) {
        var self = (Entity) (Object) this;

        if (BehaviorTriggers.hasAnyTrigger(self, BehaviorTriggers.ENTITY_INSIDE, BehaviorTriggers.JUMP_INSIDE, BehaviorTriggers.MOVE_INSIDE)) {
            var entities = this.level().getEntities(self, this.getBoundingBox().inflate(1e-7), entity -> entity instanceof LivingEntity);
            if (entities.isEmpty())
                return;

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    // trigger entity inside first
                    BehaviorTriggers.triggerBehaviors(self, living, BehaviorTriggers.ENTITY_INSIDE);

                    // trigger jump
                    if ((living instanceof ServerPlayer player && player.getLastClientInput().jump()) || living.isJumping()) {
                        BehaviorTriggers.triggerBehaviors(self, living, BehaviorTriggers.JUMP_INSIDE);
                    }

                    // trigger move
                    if (living.getDeltaMovement().horizontal().length() > 1e-7) {
                        BehaviorTriggers.triggerBehaviors(self, living, BehaviorTriggers.MOVE_INSIDE);
                    }
                }
            }
        }
    }
}
