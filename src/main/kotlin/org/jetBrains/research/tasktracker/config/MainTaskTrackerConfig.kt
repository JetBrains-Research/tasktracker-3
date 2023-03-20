package org.jetBrains.research.tasktracker.config

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import org.jetBrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetBrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetBrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetBrains.research.tasktracker.properties.PluginProperties
import java.io.File


/**
 * Plugin's main config. Initializes and stores configs of all subsystems.
 */
data class MainTaskTrackerConfig(
    val pluginProperties: PluginProperties,
    // We use a mutable list to have a change to replace configs in the list
    val configs: MutableList<BaseConfig> = mutableListOf()
) {
    companion object {
        private val logger = Logger.getInstance(MainTaskTrackerConfig::class.java)

        const val PLUGIN_NAME = "tasktracker"
        val pluginFolderPath = "${PathManager.getPluginsPath()}/${PLUGIN_NAME}"
        const val PLUGIN_PROPERTIES_FILE = "$PLUGIN_NAME.properties"

        @Suppress("UnusedPrivateMember")
        private fun List<BaseConfig>.sort(): MutableList<BaseConfig> {
            TODO("Sort handlers according to dependentHandlers")
        }

        private fun File.isConfigFile() = this.extension == "yaml"

        // TODO: add a builder for server-based properties
        fun buildConfig(pluginProperties: PluginProperties, configRoot: File): MainTaskTrackerConfig {
            val configFiles = configRoot.walkTopDown().filter { it.isConfigFile() }.toList()
            require(configFiles.isNotEmpty()) { "The root directory with config files must not be empty!" }
            val configs = configFiles.mapNotNull { configFile ->
                val fileName = configFile.name
                when {
                    fileName.startsWith(CodeTrackingConfig.CONFIG_FILE_PREFIX) -> {
                        logger.info("Building config for code tracking...")
                        CodeTrackingConfig.buildConfig(configFile)
                    }

                    fileName.startsWith(ActivityTrackingConfig.CONFIG_FILE_PREFIX) -> {
                        logger.info("Building config for activity tracking...")
                        ActivityTrackingConfig.buildConfig(configFile)
                    }

                    fileName.startsWith(MainIdeConfig.CONFIG_FILE_PREFIX) -> {
                        logger.info("Building config for ide...")
                        MainIdeConfig.buildConfig(configFile)
                    }

                    else -> {
                        logger.warn("Unknown config file: $fileName")
                        null
                    }
                }
            }

            return MainTaskTrackerConfig(
                pluginProperties = pluginProperties, configs = configs.sort()
            )
        }
    }
}
