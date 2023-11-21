package org.jetbrains.research.tasktracker.config.ide.settings

import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseProjectConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.ide.SettingsHandler
import java.io.File

@Serializable
data class SettingsConfig(
    val enableCodeCompletion: SettingMode = SettingMode.DEFAULT,
    val enableZenMode: SettingMode = SettingMode.DEFAULT,
    val theme: Theme = Theme.DEFAULT
) : BaseProjectConfig {

    override val configName: String
        get() = "settings"

    override fun buildHandler(project: Project) = SettingsHandler(this, project)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "settings"

        fun buildConfig(configFile: File): SettingsConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
