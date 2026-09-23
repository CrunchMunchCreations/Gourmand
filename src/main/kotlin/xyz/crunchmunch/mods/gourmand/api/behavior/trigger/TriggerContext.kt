package xyz.crunchmunch.mods.gourmand.api.behavior.trigger

import com.mojang.serialization.Codec
import net.minecraft.util.StringRepresentable

enum class TriggerContext(private val serialized: String) : StringRepresentable {
    INTERACTING("interacting"),
    INTERACTED("interacted"),
    ;

    override fun getSerializedName(): String = this.serialized

    companion object {
        @JvmField val CODEC: Codec<TriggerContext> = StringRepresentable.fromValues(TriggerContext::values)
    }
}
