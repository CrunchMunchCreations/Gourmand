package xyz.crunchmunch.mods.gourmand.api.behavior

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.GameType
import xyz.crunchmunch.mods.gourmand.api.behavior.trigger.BehaviorTrigger

@JvmRecord
data class TriggerableEntityBehavior @JvmOverloads constructor (
    val behavior: EntityBehavior,
    val trigger: BehaviorTrigger,
    val validGameTypes: List<GameType> = GameType.entries.toList(),
) {
    companion object {
        @JvmField
        val CODEC: Codec<TriggerableEntityBehavior> = RecordCodecBuilder.create { instance ->
            instance.group(
                EntityBehavior.CODEC.fieldOf("behavior")
                    .forGetter(TriggerableEntityBehavior::behavior),
                BehaviorTrigger.CODEC.fieldOf("trigger")
                    .forGetter(TriggerableEntityBehavior::trigger),
                GameType.CODEC.listOf().optionalFieldOf("valid_gamemodes", GameType.entries.toList())
                    .forGetter(TriggerableEntityBehavior::validGameTypes),
            )
                .apply(instance, ::TriggerableEntityBehavior)
        }
    }
}
