package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
class SolveWebPageConfig(val text: String) : BaseConfig {
    override val configName: String
        get() = "solve_web"

    companion object {
        const val CONFIG_FILE_PREFIX: String = "solve_web"

        fun buildConfig(configFile: File): SolveWebPageConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
