package xyz.crunchmunch.mods.gourmand.mixin.invisible_with_gourmand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.crunchmunch.mods.gourmand.api.GourmandEntityAttachments;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.world.entity.Entity;

@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin {
    @Inject(method = "isEntityVisible", at = @At("HEAD"), cancellable = true)
    private void hideEntitiesInvisibleForGourmand(Entity entity, Frustum frustum, double camX, double camY, double camZ, float partialTicks, long chunkFadeDuration, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasAttached(GourmandEntityAttachments.INVISIBLE_WITH_GOURMAND))
            cir.setReturnValue(false);
    }
}
