package org.jetbrains.research.tasktracker.config.ide.setting

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.LightPlatformTestCase
import org.jetbrains.research.tasktracker.config.ide.settings.SettingsConfig
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeText

class SettingsConfigTest : LightPlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTO_POPUP_COMPLETION_LOOKUP = true
    }

    fun testCodeCompletion() {
        applyConfig("codeCompletion")
        assert(!CodeInsightSettings.getInstance().AUTO_POPUP_COMPLETION_LOOKUP)
        CodeInsightSettings.getInstance().AUTO_POPUP_COMPLETION_LOOKUP = true
    }

    private fun applyConfig(configName: String) {
        val configFile = loadConfig(configName)
        val settingsConfig = SettingsConfig.buildConfig(configFile)
        settingsConfig.buildHandler().setup(project)
    }

    private fun loadConfig(name: String): File {
        val configText = SettingsConfigTest::class.java
            .getResource("$name.yaml")?.readText()
            ?: error("Cannot find `$name.yaml`!")
        val file = Files.createTempFile(name, ".yaml")
        file.writeText(configText)
        return file.toFile()
    }
}
