package org.jetBrains.research.tasktracker.config.ide

import kotlinx.serialization.Serializable
import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
class MainIdeConfig : BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "ide"

        fun buildConfig(configFile: File): MainIdeConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
