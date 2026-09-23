package xyz.crunchmunch.mods.gourmand.workarounds;

import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class NullableNotNulls {
    public static boolean matches(EntityPredicate predicate, Vec3 pos, Entity entity) {
        return predicate.matches(null, pos, entity);
    }
}
