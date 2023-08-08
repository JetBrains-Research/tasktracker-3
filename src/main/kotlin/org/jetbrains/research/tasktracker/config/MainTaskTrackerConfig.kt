package org.jetbrains.research.tasktracker.config

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.scenario.ScenarioConfig
import org.jetbrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetbrains.research.tasktracker.properties.PluginProperties
import java.io.File

/**
 * Plugin's main config. Initializes and stores configs of all subsystems.
 */
data class MainTaskTrackerConfig(
    val pluginProperties: PluginProperties,

    var taskContentConfig: TaskContentConfig? = null,
    var mainIdeConfig: MainIdeConfig? = null,
    var activityTrackingConfig: ActivityTrackingConfig? = null,
    var codeTrackingConfig: CodeTrackingConfig? = null,

    var scenarioConfig: ScenarioConfig? = null,
) {
    fun getAllConfigs() =
        listOf(taskContentConfig, mainIdeConfig, activityTrackingConfig, codeTrackingConfig, scenarioConfig)

    companion object {
        private val logger = Logger.getInstance(MainTaskTrackerConfig::class.java)

        const val PLUGIN_NAME = "tasktracker"
        val pluginFolderPath = "${PathManager.getPluginsPath()}/$PLUGIN_NAME"
        const val PLUGIN_PROPERTIES_FILE = "$PLUGIN_NAME.properties"

        private fun File.isConfigFile() = this.extension == "yaml"

        // TODO: add a builder for server-based properties
        fun buildConfig(pluginProperties: PluginProperties, configRoot: File): MainTaskTrackerConfig {
            val mainConfig = MainTaskTrackerConfig(pluginProperties)

            val configFiles = configRoot.walkTopDown().filter { it.isConfigFile() }.toList()
            require(configFiles.isNotEmpty()) { "The root directory with config files must not be empty!" }
            configFiles.forEach { configFile ->
                val fileName = configFile.name
                when {
                    fileName.startsWith(CodeTrackingConfig.CONFIG_FILE_PREFIX) -> {
                        require(mainConfig.codeTrackingConfig == null) {
                            "The code tracking config was already parsed"
                        }
                        logger.info("Building config for code tracking...")
                        mainConfig.codeTrackingConfig = CodeTrackingConfig.buildConfig(configFile)
                    }

                    fileName.startsWith(ActivityTrackingConfig.CONFIG_FILE_PREFIX) -> {
                        require(mainConfig.activityTrackingConfig == null) {
                            "The activity tracking config was already parsed"
                        }
                        logger.info("Building config for activity tracking...")
                        mainConfig.activityTrackingConfig = ActivityTrackingConfig.buildConfig(configFile)
                    }

                    fileName.startsWith(MainIdeConfig.CONFIG_FILE_PREFIX) -> {
                        require(mainConfig.mainIdeConfig == null) { "The main IDE config was already parsed" }
                        logger.info("Building config for ide...")
                        mainConfig.mainIdeConfig = MainIdeConfig.buildConfig(configFile)
                    }

                    fileName.startsWith(TaskContentConfig.CONFIG_FILE_PREFIX) -> {
                        require(mainConfig.taskContentConfig == null) {
                            "The task content config was already parsed"
                        }
                        logger.info("Building task content config...")
                        mainConfig.taskContentConfig = TaskContentConfig.buildConfig(configFile)
                    }

                    fileName.startsWith(ScenarioConfig.CONFIG_FILE_PREFIX) -> {
                        require(mainConfig.scenarioConfig == null) {
                            "The scenario config was already parsed"
                        }
                        logger.info("Building scenario config...")
                        mainConfig.scenarioConfig = ScenarioConfig.buildConfig(configFile)
                    }
                }
            }

            return mainConfig
        }
    }
}
