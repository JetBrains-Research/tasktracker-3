package org.jetbrains.research.tasktracker.config

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.scenario.ScenarioConfig
import org.jetbrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetbrains.research.tasktracker.properties.PropertiesController
import org.jetbrains.research.tasktracker.tracking.TaskFileHandler
import java.io.File

object DefaultConfigsFactory {
    private val names = listOf("default")
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
            names.forEach { name ->
                val configName = "${configPrefix}_$name.yaml"
                val directoryPath = "${PropertiesController.defaultConfigRoot}/$name"
                val directory = File(directoryPath)
                if(!directory.exists()){
                    directory.mkdir()
                }
                val configFile = File("$directoryPath/$configName")
                if (!configFile.exists()) {
                    configFile.writeText(
                        DefaultConfigsFactory::class.java.getResource(configName)?.readText()
                            ?: error("default $configName config must exist")
                    )
                }
            }
        }
        logger.info("default configs created")
    }
}
