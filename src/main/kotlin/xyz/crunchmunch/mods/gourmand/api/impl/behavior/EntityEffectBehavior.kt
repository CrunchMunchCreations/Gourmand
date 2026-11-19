package xyz.crunchmunch.mods.gourmand.api.impl.behavior

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import xyz.crunchmunch.mods.gourmand.api.behavior.EntityBehavior

@JvmRecord
data class EntityEffectBehavior @JvmOverloads constructor (
    val effect: Holder<MobEffect>,
    val durationTicks: Int,
    val amplifier: Int = 0,
    val isAmbient: Boolean = false,
    val isHidden: Boolean = false,
    val shouldShowIcon: Boolean = true,
) : EntityBehavior {
    override fun handle(interactedEntity: Entity, interactingEntity: LivingEntity) {
        interactingEntity.addEffect(
            MobEffectInstance(
                this.effect,
                this.durationTicks,
                this.amplifier,
                this.isAmbient,
                !this.isHidden,
                this.shouldShowIcon
            ), interactedEntity)
    }

    override val codec: MapCodec<out EntityBehavior>
        get() = CODEC

    companion object {
        @JvmField val CODEC: MapCodec<EntityEffectBehavior> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                MobEffect.CODEC.fieldOf("effect")
                    .forGetter(EntityEffectBehavior::effect),
                Codec.INT.fieldOf("duration_ticks")
                    .forGetter(EntityEffectBehavior::durationTicks),
                Codec.INT.optionalFieldOf("amplifier", 0)
                    .forGetter(EntityEffectBehavior::amplifier),
                Codec.BOOL.optionalFieldOf("ambient", false)
                    .forGetter(EntityEffectBehavior::isAmbient),
                Codec.BOOL.optionalFieldOf("hidden", false)
                    .forGetter(EntityEffectBehavior::isHidden),
                Codec.BOOL.optionalFieldOf("show_icon", true)
                    .forGetter(EntityEffectBehavior::shouldShowIcon),
            )
                .apply(instance, ::EntityEffectBehavior)
        }
    }
}
