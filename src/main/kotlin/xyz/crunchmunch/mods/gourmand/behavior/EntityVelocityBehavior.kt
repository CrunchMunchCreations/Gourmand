package xyz.crunchmunch.mods.gourmand.behavior

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import xyz.crunchmunch.mods.gourmand.api.behavior.EntityBehavior

data class EntityVelocityBehavior(
    val velocity: Vec3,
    val type: Type,
    val transformSpace: TransformSpace,
) : EntityBehavior {
    override fun handle(
        interactedEntity: Entity,
        interactingEntity: LivingEntity
    ) {
        val velocity = this.transformSpace.transformSpaceToGlobal(this.velocity, interactingEntity)
        when (this.type) {
            Type.ADD -> interactingEntity.deltaMovement = interactingEntity.deltaMovement.add(velocity)
            Type.MULTIPLY -> interactingEntity.deltaMovement = interactingEntity.deltaMovement.multiply(velocity)
            Type.SET -> interactingEntity.deltaMovement = velocity
        }
    }

    override val codec: MapCodec<out EntityBehavior> = CODEC

    companion object {
        @JvmField val CODEC: MapCodec<EntityVelocityBehavior> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Vec3.CODEC.fieldOf("velocity")
                    .forGetter(EntityVelocityBehavior::velocity),
                Type.CODEC.optionalFieldOf("type", Type.ADD)
                    .forGetter(EntityVelocityBehavior::type),
                TransformSpace.CODEC.optionalFieldOf("transform_space", TransformSpace.WORLD)
                    .forGetter(EntityVelocityBehavior::transformSpace)
            )
                .apply(instance, ::EntityVelocityBehavior)
        }

        enum class Type(private val serialized: String) : StringRepresentable {
            ADD("add"), MULTIPLY("multiply"), SET("set"),
            ;

            override fun getSerializedName(): String = this.serialized

            companion object {
                @JvmField val CODEC: Codec<Type> = StringRepresentable.fromValues(Type::values)
            }
        }

        // Based on Apoli's Space - https://github.com/BluSpring/Apoli-Legacy/blob/versions/1.21.11/src/main/java/io/github/apace100/apoli/util/Space.java
        enum class TransformSpace(private val serialized: String, val transformSpaceToGlobal: (Vec3, Entity) -> Vec3) : StringRepresentable {
            WORLD("world", { vector, _ -> vector }),
            LOCAL("local", { vector, entity -> Vec3.applyLocalCoordinatesToRotation(entity.rotationVector, vector) })
            ;

            override fun getSerializedName(): String = this.serialized

            companion object {
                @JvmField val CODEC: Codec<TransformSpace> = StringRepresentable.fromValues(TransformSpace::values)
            }
        }
    }
}
