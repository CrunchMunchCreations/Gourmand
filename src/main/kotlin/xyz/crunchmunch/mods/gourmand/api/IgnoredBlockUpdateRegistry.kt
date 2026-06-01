package xyz.crunchmunch.mods.gourmand.api

import net.minecraft.world.level.block.state.BlockState
import java.util.*

object IgnoredBlockUpdateRegistry {
    private val blockStates = mutableListOf<BlockState>()

    @JvmStatic
    val states: List<BlockState> = Collections.unmodifiableList(this.blockStates)

    @JvmStatic
    fun registerState(state: BlockState) {
        if (!this.blockStates.contains(state))
            this.blockStates.add(state)
    }

    @JvmStatic
    fun isStateIgnored(state: BlockState): Boolean = this.blockStates.contains(state)

    @JvmStatic
    fun resetStates() = this.blockStates.clear()
}
