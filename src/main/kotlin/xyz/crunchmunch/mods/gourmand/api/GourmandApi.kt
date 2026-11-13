package xyz.crunchmunch.mods.gourmand.api

import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import xyz.crunchmunch.mods.gourmand.api.debug.DebugArea
import xyz.crunchmunch.mods.gourmand.api.debug.DebugPath

object GourmandApi {
    fun ServerLevel.setDebugPath(id: Identifier, path: DebugPath) {
        val existingPathMap = this.getAttachedOrCreate(GourmandLevelAttachments.DEBUG_PATHS).toMutableMap()
        existingPathMap[id] = path
        this.setAttached(GourmandLevelAttachments.DEBUG_PATHS, existingPathMap)
    }

    fun ServerLevel.removeDebugPath(id: Identifier) {
        val existingPathMap = this.getAttachedOrCreate(GourmandLevelAttachments.DEBUG_PATHS).toMutableMap()
        existingPathMap.remove(id)

        this.setAttached(GourmandLevelAttachments.DEBUG_PATHS, existingPathMap)
    }

    fun ServerLevel.setDebugArea(id: Identifier, area: DebugArea) {
        val existingAreaMap = this.getAttachedOrCreate(GourmandLevelAttachments.DEBUG_AREAS).toMutableMap()
        existingAreaMap[id] = area

        this.setAttached(GourmandLevelAttachments.DEBUG_AREAS, existingAreaMap)
    }

    fun ServerLevel.removeDebugArea(id: Identifier) {
        val existingAreaMap = this.getAttachedOrCreate(GourmandLevelAttachments.DEBUG_AREAS).toMutableMap()
        existingAreaMap.remove(id)

        this.setAttached(GourmandLevelAttachments.DEBUG_AREAS, existingAreaMap)
    }
}
