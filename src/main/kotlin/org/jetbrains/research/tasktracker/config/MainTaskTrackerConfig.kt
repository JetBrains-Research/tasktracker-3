package org.jetbrains.research.tasktracker.config

import com.intellij.openapi.application.PathManager
import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.scenario.ScenarioConfig
import org.jetbrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetbrains.research.tasktracker.config.util.ConfigUtil
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
                        mainConfig.codeTrackingConfig = ConfigUtil.buildConfig(
                            mainConfig.codeTrackingConfig,
                            "code_tracking",
                            configFile,
                            CodeTrackingConfig::buildConfig
                        )
                    }

                    fileName.startsWith(ActivityTrackingConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.activityTrackingConfig = ConfigUtil.buildConfig(
                            mainConfig.activityTrackingConfig,
                            "activity_tracking",
                            configFile,
                            ActivityTrackingConfig::buildConfig
                        )
                    }

                    fileName.startsWith(TaskContentConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.taskContentConfig = ConfigUtil.buildConfig(
                            mainConfig.taskContentConfig,
                            "task_content",
                            configFile,
                            TaskContentConfig::buildConfig
                        )
                    }

                    fileName.startsWith(ScenarioConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.scenarioConfig = ConfigUtil.buildConfig(
                            mainConfig.scenarioConfig,
                            "scenario",
                            configFile,
                            ScenarioConfig::buildConfig
                        )
                    }
                }
            }
            mainConfig.mainIdeConfig = MainIdeConfig.buildConfig(configFiles)
            return mainConfig
        }
    }
}
