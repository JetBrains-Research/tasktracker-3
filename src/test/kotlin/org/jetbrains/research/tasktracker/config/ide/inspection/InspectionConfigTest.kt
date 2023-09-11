package org.jetbrains.research.tasktracker.config.ide.inspection

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.codeInspection.ex.InspectionProfileImpl
import com.intellij.profile.codeInspection.InspectionProfileManager
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager
import com.intellij.testFramework.LightPlatformTestCase
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeText

class InspectionConfigTest : LightPlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTO_POPUP_COMPLETION_LOOKUP = true
    }

    fun testAllInspections() {
        val profile = setupProjectProfile()
        val configFile = loadConfig("all")
        applyConfig(configFile, profile)
        assert(profile.allTools.all { it.isEnabled }) {
            "all available tools must be enabled, the list of disabled tools: ${profile.getAllDisabledToolIds()}"
        }
    }

    private fun InspectionProfileImpl.getAllDisabledToolIds() = allTools.filter { !it.isEnabled }.map { it.tool.id }

    private fun applyConfig(configFile: File, profile: InspectionProfileImpl) {
        val inspectionConfig = InspectionConfig.buildConfig(configFile)
        inspectionConfig.buildHandler().applyConfig(profile, project)
    }

    private fun loadConfig(name: String): File {
        val configText = InspectionConfigTest::class.java
            .getResource("$name.yaml")?.readText()
            ?: error("Cannot find codeTrackingConfig.yaml!")
        val file = Files.createTempFile(name, ".yaml")
        file.writeText(configText)
        return file.toFile()
    }

    /**
     * copy inspections from the global profile to the project profile
     */
    private fun setupProjectProfile(): InspectionProfileImpl {
        InspectionProfileImpl.INIT_INSPECTIONS = true
        val inspectionProfileManager = ProjectInspectionProfileManager.getInstance(project)
        val profile = InspectionProfileManager.getInstance().currentProfile
        inspectionProfileManager.currentProfile.copyFrom(profile)
        return InspectionProfileManager.getInstance().currentProfile
    }
}
