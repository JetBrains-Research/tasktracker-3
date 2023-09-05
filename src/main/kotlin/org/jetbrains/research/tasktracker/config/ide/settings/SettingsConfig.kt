package org.jetbrains.research.tasktracker.config.ide.settings

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.ide.SettingsHandler
import java.io.File

@Serializable
data class SettingsConfig(val enableCodeCompletion: SettingMode) : BaseConfig {

    override fun buildHandler() = SettingsHandler(this)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "settings"

        fun buildConfig(configFile: File): SettingsConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
