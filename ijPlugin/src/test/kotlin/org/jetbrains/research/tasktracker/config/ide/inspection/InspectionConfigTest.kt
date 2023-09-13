package org.jetbrains.research.tasktracker.config.ide.inspection

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.codeInspection.ex.InspectionProfileImpl
import com.intellij.profile.codeInspection.InspectionProfileManager
import com.intellij.testFramework.LightPlatformTestCase
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeText

class InspectionConfigTest : LightPlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTO_POPUP_COMPLETION_LOOKUP = true
        setupProjectProfile()
    }

    fun testAllInspections() {
        val profile = getProfile("all")
        assert(profile.allTools.all { it.isEnabled }) {
            "all available tools must be enabled, the list of disabled tools: ${profile.getAllDisabledToolIds()}"
        }
    }

    fun testNoneInspections() {
        val profile = getProfile("none")
        assert(profile.allTools.all { !it.isEnabled }) {
            "all available tools must be disabled, the list of disabled tools: ${profile.getAllEnabledToolIds()}"
        }
    }

    fun testDefaultInspections() {
        val profile = getProfile("default")
        val enabledToolsSize = profile.allTools.count { it.isEnabled }
        assert(enabledToolsSize == DEFAULT_ENABLED_INSPECTION_SIZE) {
            "default enabled tools size is $DEFAULT_ENABLED_INSPECTION_SIZE, but $enabledToolsSize has been found"
        }
    }

    fun testEnableSelectedInspections() {
        val profile = getProfile("enableSelected")
        val enabledTools = profile.getAllEnabledToolIds()
        assert(enabledTools.size == enableSelectedTools.size) {
            "enable selected tools size is ${enableSelectedTools.size}, but ${enabledTools.size} has been found"
        }
        assert(enabledTools.containsAll(enableSelectedTools)) {
            "enable selected tools must be $enableSelectedTools, but $enabledTools have been found"
        }
    }

    fun testDisableSelectedInspections() {
        val profile = getProfile("disableSelected")
        val enabledTools = profile.getAllEnabledToolIds()
        assert(enabledTools.size == DISABLE_SELECTED_ENABLED_INSPECTION_SIZE) {
            buildString {
                append("disable selected tools size is $DISABLE_SELECTED_ENABLED_INSPECTION_SIZE")
                append(", but ${enabledTools.size} has been found")
            }
        }
        assert(disableSelectedTools.all { !enabledTools.contains(it) }) {
            "disable selected tools must not contains $disableSelectedTools, but $enabledTools have been found"
        }
    }

    fun testAddSelectedInspections() {
        val profile = getProfile("addSelected")
        val enabledTools = profile.getAllEnabledToolIds()
        println(profile.getAllDisabledToolIds())
        assert(enabledTools.size == ADD_SELECTED_ENABLED_INSPECTION_SIZE) {
            "add selected tools size is $ADD_SELECTED_ENABLED_INSPECTION_SIZE, but ${enabledTools.size} has been found"
        }
        assert(enabledTools.containsAll(addSelectedTools)) {
            "add selected tools must contains $addSelectedTools, but ${enabledTools.size} has been found"
        }
    }

    private fun getProfile(configName: String): InspectionProfileImpl {
        val profile = InspectionProfileManager.getInstance().currentProfile
        val configFile = loadConfig(configName)
        applyConfig(configFile, profile)
        return profile
    }

    private fun InspectionProfileImpl.getAllDisabledToolIds() = allTools.filter { !it.isEnabled }.map { it.tool.id }

    private fun InspectionProfileImpl.getAllEnabledToolIds() = allTools.filter { it.isEnabled }.map { it.tool.id }

    private fun applyConfig(configFile: File, profile: InspectionProfileImpl) {
        val inspectionConfig = InspectionConfig.buildConfig(configFile)
        inspectionConfig.buildHandler().applyConfig(profile, project)
    }

    private fun loadConfig(name: String): File {
        val configText = InspectionConfigTest::class.java
            .getResource("$name.yaml")?.readText()
            ?: error("Cannot find `$name.yaml`!")
        val file = Files.createTempFile(name, ".yaml")
        file.writeText(configText)
        return file.toFile()
    }

    /**
     * copy inspections from the global profile to the project profile
     */
    private fun setupProjectProfile(): InspectionProfileImpl {
        InspectionProfileImpl.INIT_INSPECTIONS = true
        val profile = InspectionProfileManager.getInstance().currentProfile
        profile.resetInspections()
        return profile
    }

    private fun InspectionProfileImpl.resetInspections() {
        defaultTools.apply {
            if (isEmpty()) {
                addAll(getAllEnabledToolIds())
            } else {
                disableAllTools(project)
                forEach {
                    enableTool(it, project)
                }
            }
        }
    }

    companion object {
        const val DEFAULT_ENABLED_INSPECTION_SIZE = 55
        const val DISABLE_SELECTED_ENABLED_INSPECTION_SIZE = 53
        const val ADD_SELECTED_ENABLED_INSPECTION_SIZE = 57
        val enableSelectedTools = listOf("LongLine", "TodoComment")
        val disableSelectedTools = listOf("CheckTagEmptyBody", "RegExpRedundantEscape")
        val addSelectedTools = listOf("EmptyDirectory", "ProblematicWhitespace")
        val defaultTools = mutableListOf<String>()
    }
}
