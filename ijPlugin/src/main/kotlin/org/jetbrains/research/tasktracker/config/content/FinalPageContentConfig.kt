package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
class FinalPageContentConfig(val text: String) : BaseConfig {
    override val configName: String
        get() = CONFIG_FILE_PREFIX

    companion object {
        const val CONFIG_FILE_PREFIX: String = "final_page"

        fun buildConfig(configFile: File): FinalPageContentConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
