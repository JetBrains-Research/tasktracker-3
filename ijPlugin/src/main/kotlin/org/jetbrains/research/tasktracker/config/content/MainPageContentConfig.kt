package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
class MainPageContentConfig(val pluginName: String, val pluginDescription: String) : BaseConfig {
    override val configName: String
        get() = "main_page"

    companion object {
        const val CONFIG_FILE_PREFIX: String = "main_page"

        fun buildConfig(configFile: File): MainPageContentConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
