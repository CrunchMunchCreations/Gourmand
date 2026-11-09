package xyz.crunchmunch.mods.gourmand.mixin.track_vehicle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("dimensions")
    EntityDimensions gourmand$getDimensions();

    @Invoker
    Vec3 callGetPassengerAttachmentPoint(final Entity passenger, final EntityDimensions dimensions, final float scale);
}
