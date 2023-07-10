package org.jetbrains.research.tasktracker.config.ide

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
class MainIdeConfig : BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "ide"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: File): MainIdeConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
