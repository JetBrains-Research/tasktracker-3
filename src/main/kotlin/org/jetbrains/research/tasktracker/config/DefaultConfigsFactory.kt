package org.jetbrains.research.tasktracker.config

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.scenario.ScenarioConfig
import org.jetbrains.research.tasktracker.config.tasksInfo.TasksInfoConfig.Companion.TASKS_INFO_CONFIG_NAME
import org.jetbrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetbrains.research.tasktracker.properties.PropertiesController
import java.io.File

object DefaultConfigsFactory {
    private val logger: Logger = Logger.getInstance(DefaultConfigsFactory.javaClass)

    private val configNames = listOf(
        ActivityTrackingConfig.CONFIG_FILE_PREFIX,
        CodeTrackingConfig.CONFIG_FILE_PREFIX,
        MainIdeConfig.CONFIG_FILE_PREFIX,
        TaskContentConfig.CONFIG_FILE_PREFIX,
        ScenarioConfig.CONFIG_FILE_PREFIX
    )

    fun createDefaultConfigs() {
        TaskTrackerPlugin.tasksInfoConfig.configNames.forEach { name ->
            val directoryPath = "${PropertiesController.configRoot}/$name"
            val directory = File(directoryPath)
            if (!directory.exists()) {
                directory.mkdir()
            }
            configNames.forEach { configPrefix ->
                val configName = "${configPrefix}_$name.yaml"
                writeFromResources(configName, "$directoryPath/$configName")
            }
        }
        logger.info("default configs created")
    }

    fun createAvailableTasksConfig() {
        writeFromResources(TASKS_INFO_CONFIG_NAME, "${PropertiesController.configRoot}/$TASKS_INFO_CONFIG_NAME")
    }

    private fun writeFromResources(configName: String, filePath: String) {
        val configFile = File(filePath)
        if (!configFile.exists()) {
            configFile.writeText(
                DefaultConfigsFactory::class.java.getResource(configName)?.readText()
                    ?: error("default $configName config must exist")
            )
        }
    }
}
