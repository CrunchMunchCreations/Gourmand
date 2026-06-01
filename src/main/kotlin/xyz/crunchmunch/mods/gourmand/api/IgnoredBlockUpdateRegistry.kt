package xyz.crunchmunch.mods.gourmand.api

import net.minecraft.world.level.block.state.BlockState

object IgnoredBlockUpdateRegistry {
    private val blockStates = mutableSetOf<BlockState>()

    @JvmStatic
    fun registerState(state: BlockState) {
        this.blockStates.add(state)
    }

    @JvmStatic
    fun isStateIgnored(state: BlockState): Boolean = this.blockStates.contains(state)
}
