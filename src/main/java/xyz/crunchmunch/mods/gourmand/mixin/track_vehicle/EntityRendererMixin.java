package xyz.crunchmunch.mods.gourmand.mixin.track_vehicle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import xyz.crunchmunch.mods.gourmand.api.GourmandEntityAttachments;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @WrapOperation(method = "extractRenderState", at = {
        @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;xOld:D", opcode = Opcodes.GETFIELD),
    }, slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z")))
    private double useVehicleAttachmentAsPositionRefXOld(Entity instance, Operation<Double> original) {
        if (instance.hasAttached(GourmandEntityAttachments.TRACK_VEHICLE) && instance.isPassenger()) {
            var vehicle = instance.getVehicle();

            return vehicle.xOld + ((EntityAccessor) vehicle).callGetPassengerAttachmentPoint(instance, ((EntityAccessor) vehicle).gourmand$getDimensions(), 1f).x();
        }

        return original.call(instance);
    }

    @WrapOperation(method = "extractRenderState", at = {
        @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;yOld:D", opcode = Opcodes.GETFIELD),
    }, slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z")))
    private double useVehicleAttachmentAsPositionRefYOld(Entity instance, Operation<Double> original) {
        if (instance.hasAttached(GourmandEntityAttachments.TRACK_VEHICLE) && instance.isPassenger()) {
            var vehicle = instance.getVehicle();

            return vehicle.yOld + ((EntityAccessor) vehicle).callGetPassengerAttachmentPoint(instance, ((EntityAccessor) vehicle).gourmand$getDimensions(), 1f).y();
        }

        return original.call(instance);
    }

    @WrapOperation(method = "extractRenderState", at = {
        @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;zOld:D", opcode = Opcodes.GETFIELD),
    }, slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z")))
    private double useVehicleAttachmentAsPositionRefZOld(Entity instance, Operation<Double> original) {
        if (instance.hasAttached(GourmandEntityAttachments.TRACK_VEHICLE) && instance.isPassenger()) {
            var vehicle = instance.getVehicle();

            return vehicle.zOld + ((EntityAccessor) vehicle).callGetPassengerAttachmentPoint(instance, ((EntityAccessor) vehicle).gourmand$getDimensions(), 1f).z();
        }

        return original.call(instance);
    }

    @WrapOperation(method = "extractRenderState", at = {
        @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getX()D"),
    }, slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z")))
    private double useVehicleAttachmentAsPositionRefX(Entity instance, Operation<Double> original) {
        if (instance.hasAttached(GourmandEntityAttachments.TRACK_VEHICLE) && instance.isPassenger()) {
            var vehicle = instance.getVehicle();

            return vehicle.getX() + ((EntityAccessor) vehicle).callGetPassengerAttachmentPoint(instance, ((EntityAccessor) vehicle).gourmand$getDimensions(), 1f).x();
        }

        return original.call(instance);
    }

    @WrapOperation(method = "extractRenderState", at = {
        @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getY()D"),
    }, slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z")))
    private double useVehicleAttachmentAsPositionRefY(Entity instance, Operation<Double> original) {
        if (instance.hasAttached(GourmandEntityAttachments.TRACK_VEHICLE) && instance.isPassenger()) {
            var vehicle = instance.getVehicle();

            return vehicle.getY() + ((EntityAccessor) vehicle).callGetPassengerAttachmentPoint(instance, ((EntityAccessor) vehicle).gourmand$getDimensions(), 1f).y();
        }

        return original.call(instance);
    }

    @WrapOperation(method = "extractRenderState", at = {
        @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getZ()D"),
    }, slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z")))
    private double useVehicleAttachmentAsPositionRefZ(Entity instance, Operation<Double> original) {
        if (instance.hasAttached(GourmandEntityAttachments.TRACK_VEHICLE) && instance.isPassenger()) {
            var vehicle = instance.getVehicle();

            return vehicle.getZ() + ((EntityAccessor) vehicle).callGetPassengerAttachmentPoint(instance, ((EntityAccessor) vehicle).gourmand$getDimensions(), 1f).z();
        }

        return original.call(instance);
    }
}
