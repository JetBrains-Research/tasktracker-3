package org.jetbrains.research.tasktracker.config.ide.inspection

import com.intellij.codeInspection.ex.InspectionProfileImpl
import com.intellij.profile.codeInspection.InspectionProfileManager
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager
import com.intellij.testFramework.builders.ModuleFixtureBuilder
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeText

class InspectionConfigTest : CodeInsightFixtureTestCase<ModuleFixtureBuilder<*>>() {

    override fun setUp() {
        super.setUp()
        InspectionProfileImpl.INIT_INSPECTIONS = true
        setupProjectProfile()
    }

    fun testAllInspections() {
        val configFile = loadConfig("all")
        applyConfig(configFile)
        val profile = InspectionProfileManager.getInstance(project).currentProfile
        assert(profile.getAllEnabledInspectionTools(project).size == profile.allTools.size) {
            "all available tools must be enabled"
        }
    }

    private fun applyConfig(configFile: File) {
        val inspectionConfig = InspectionConfig.buildConfig(configFile)
        inspectionConfig.buildHandler().setup(project)
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
    private fun setupProjectProfile() {
        val inspectionProfileManager = ProjectInspectionProfileManager.getInstance(project)
        val profile = InspectionProfileManager.getInstance().currentProfile
        inspectionProfileManager.currentProfile.copyFrom(profile)
    }
}
