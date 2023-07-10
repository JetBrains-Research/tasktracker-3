package org.jetBrains.research.tasktracker.config

import org.jetBrains.research.tasktracker.config.content.TaskContentConfig
import org.jetBrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetBrains.research.tasktracker.config.scenario.ScenarioConfig
import org.jetBrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetBrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetBrains.research.tasktracker.properties.PropertiesController
import java.io.File

object DefaultConfigsFactory {

    private val configNames = listOf(
        ActivityTrackingConfig.CONFIG_FILE_PREFIX,
        CodeTrackingConfig.CONFIG_FILE_PREFIX,
        MainIdeConfig.CONFIG_FILE_PREFIX,
        TaskContentConfig.CONFIG_FILE_PREFIX,
        ScenarioConfig.CONFIG_FILE_PREFIX
    )

    fun createDefaultConfigs() { // TODO rename?
        configNames.forEach { configPrefix ->
            val configName = "${configPrefix}_default.yaml"
            val configFile = File("${PropertiesController.defaultConfigRoot}/$configName")
            if (!configFile.exists()) {
                configFile.writeText(
                    javaClass.classLoader.getResource("configs/$configName")?.readText()
                        ?: error("default config must exist")
                )
            }
        }
    }
}
