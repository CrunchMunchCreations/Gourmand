package xyz.crunchmunch.mods.gourmand.mixin.allow_collision;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.crunchmunch.mods.gourmand.api.GourmandEntityAttachments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;

@Mixin(AbstractBoat.class)
public abstract class AbstractBoatMixin {
    @ModifyReturnValue(method = "canVehicleCollide", at = @At("RETURN"))
    private static boolean checkIsEitherCollidable(boolean original, Entity vehicle, Entity entity) {
        return original && vehicle.getAttachedOrCreate(GourmandEntityAttachments.ALLOW_BOAT_COLLISION) && entity.getAttachedOrCreate(GourmandEntityAttachments.ALLOW_BOAT_COLLISION);
    }
}
