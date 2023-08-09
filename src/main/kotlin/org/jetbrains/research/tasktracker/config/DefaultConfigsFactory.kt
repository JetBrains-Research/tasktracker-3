package org.jetbrains.research.tasktracker.config

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.scenario.ScenarioConfig
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
        configNames.forEach { configPrefix ->
            val configName = "${configPrefix}_default.yaml"
            writeFromResources(configName, "${PropertiesController.defaultConfigRoot.absolutePath}/$configName")
        }
        logger.info("default configs created")
    }

    private fun writeFromResources(configName: String, filePath: String) {
        val configFile = File(filePath)
        if (!configFile.exists()) {
            DefaultConfigsFactory::class.java.getResource(configName)?.readText()?.let {
                configFile.writeText(it)
            } ?: logger.warn("There are no file with name $configName")
        }
    }
}
