package xyz.crunchmunch.mods.gourmand.api.impl.predicate

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.predicates.entity.EntitySubPredicate
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

@JvmRecord
data class HasEntityTagPredicate(val tag: String) : EntitySubPredicate {
    override fun matches(entity: Entity, level: ServerLevel, position: Vec3?): Boolean {
        return entity.entityTags().contains(this.tag)
    }

    companion object {
        @JvmField val CODEC: Codec<HasEntityTagPredicate> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("tag")
                    .forGetter(HasEntityTagPredicate::tag)
            )
                .apply(instance, ::HasEntityTagPredicate)
        }
    }
}
