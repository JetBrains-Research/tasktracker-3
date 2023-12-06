package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
class PluginInfoConfig(val pluginName: String, val pluginDescription: String) : BaseConfig {
    override val configName: String
        get() = CONFIG_FILE_PREFIX

    companion object {
        const val CONFIG_FILE_PREFIX: String = "info"

        fun buildConfig(configFile: File): PluginInfoConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
