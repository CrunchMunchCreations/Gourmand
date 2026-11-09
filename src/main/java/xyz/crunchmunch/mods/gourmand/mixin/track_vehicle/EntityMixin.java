package xyz.crunchmunch.mods.gourmand.mixin.track_vehicle;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.crunchmunch.mods.gourmand.api.GourmandEntityAttachments;

import net.minecraft.world.entity.Entity;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;

@Mixin(Entity.class)
public abstract class EntityMixin implements AttachmentTarget {
    @Shadow
    public abstract boolean isPassenger();

    @Shadow
    public abstract @Nullable Entity getVehicle();

    @Inject(method = "getXRot(F)F", at = @At("HEAD"), cancellable = true)
    private void tryTrackVehicleRotationPitch(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (this.isPassenger() && this.hasAttached(GourmandEntityAttachments.TRACK_VEHICLE)) {
            cir.setReturnValue(this.getVehicle().getXRot(partialTicks));
        }
    }

    @Inject(method = "getYRot(F)F", at = @At("HEAD"), cancellable = true)
    private void tryTrackVehicleRotationYaw(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (this.isPassenger() && this.hasAttached(GourmandEntityAttachments.TRACK_VEHICLE)) {
            cir.setReturnValue(this.getVehicle().getYRot(partialTicks));
        }
    }
}
