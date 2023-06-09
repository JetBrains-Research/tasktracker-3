package org.jetBrains.research.tasktracker.config.scenario

import kotlinx.serialization.Serializable
import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
data class ScenarioConfig(val scenario: Scenario) : BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "scenario"

        fun buildConfig(configFile: File): ScenarioConfig {
            // TODO: if we work with local config check that the scenario has only one step
            return YamlConfigLoadStrategy.load(configFile.readText(), serializer())
        }
    }
}
