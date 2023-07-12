package org.jetbrains.research.tasktracker.config

import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.scenario.ScenarioConfig
import org.jetbrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetbrains.research.tasktracker.properties.PropertiesController
import java.io.File

object DefaultConfigsFactory {

    private val configNames = listOf(
        ActivityTrackingConfig.CONFIG_FILE_PREFIX,
        CodeTrackingConfig.CONFIG_FILE_PREFIX,
        MainIdeConfig.CONFIG_FILE_PREFIX,
        TaskContentConfig.CONFIG_FILE_PREFIX,
        ScenarioConfig.CONFIG_FILE_PREFIX
    )

    fun createDefaultConfigs() {
        configNames.forEach { configPrefix ->
            val configName = "${configPrefix}_default.yaml"
            val configFile = File("${PropertiesController.defaultConfigRoot}/$configName")
            if (!configFile.exists()) {
                configFile.writeText(
                    DefaultConfigsFactory::class.java.getResource(configName)?.readText()
                        ?: error("default $configName config must exist")
                )
            }
        }
    }
}
