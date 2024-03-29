package org.jetbrains.research.tasktracker.config.scenario

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.config.scenario.models.Scenario
import java.io.File

@Serializable
data class ScenarioConfig(val scenario: Scenario) : BaseConfig {

    override val configName: String
        get() = "scenario"

    val test: String = "" // TODO make real fields
    companion object {
        const val CONFIG_FILE_PREFIX: String = "scenario"

        fun buildConfig(configFile: File): ScenarioConfig {
            // TODO: if we work with local config check that the scenario has only one step
            return YamlConfigLoadStrategy.load(configFile.readText(), serializer())
        }
    }
}
