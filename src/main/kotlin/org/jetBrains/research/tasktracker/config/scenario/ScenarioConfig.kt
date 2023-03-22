package org.jetBrains.research.tasktracker.config.scenario

import org.jetBrains.research.tasktracker.config.BaseConfig
import java.io.File

data class ScenarioConfig(val scenario: Scenario) : BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "scenario"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: File): ScenarioConfig {
            // TODO: if we work with local config check that the scenario has only one step
            TODO("Not implemented yet")
        }
    }
}
