package xyz.crunchmunch.mods.gourmand.api.behavior

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.Identifier
import net.minecraft.world.level.GameType

@JvmRecord
data class TriggerableEntityBehavior(
    val behavior: EntityBehavior,
    val trigger: Identifier,
    val validGameTypes: List<GameType>,
) {
    companion object {
        @JvmField
        val CODEC: Codec<TriggerableEntityBehavior> = RecordCodecBuilder.create { instance ->
            instance.group(
                EntityBehavior.CODEC.fieldOf("behavior")
                    .forGetter(TriggerableEntityBehavior::behavior),
                Identifier.CODEC.fieldOf("trigger")
                    .forGetter(TriggerableEntityBehavior::trigger),
                GameType.CODEC.listOf().optionalFieldOf("valid_gamemodes", GameType.entries)
                    .forGetter(TriggerableEntityBehavior::validGameTypes),
            )
                .apply(instance, ::TriggerableEntityBehavior)
        }
    }
}
