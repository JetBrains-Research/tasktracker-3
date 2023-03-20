package org.jetBrains.research.tasktracker.config

import com.intellij.openapi.diagnostic.Logger
import org.jetBrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetBrains.research.tasktracker.config.storage.LocalStorageConfig
import org.jetBrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetBrains.research.tasktracker.config.tracking.CodeTrackingConfig
import java.nio.file.Path

enum class TestMode {
    ON, OFF
}

/**
 * Plugin's main config. Initializes and stores configs of all subsystems.
 */
data class MainTaskTrackerConfig(
    val testMode: TestMode = TestMode.ON,
    val configs: List<BaseConfig> = emptyList()
) {
    companion object {
        private val logger = Logger.getInstance(MainTaskTrackerConfig::class.java)

        const val PLUGIN_NAME = "tasktracker"

        @Suppress("UnusedPrivateMember")
        private fun sortConfigHandlers(configs: List<BaseConfig>): List<BaseConfig> {
            TODO("Sort handlers according to dependentHandlers")
        }

        fun buildConfig(vararg configFiles: Path): MainTaskTrackerConfig {
            val configs = configFiles.mapNotNull { configFile ->
                val fileName = configFile.fileName.toString()
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

                    fileName.startsWith(LocalStorageConfig.CONFIG_FILE_PREFIX) -> {
                        logger.info("Building config for local storage...")
                        LocalStorageConfig.buildConfig(configFile)
                    }

                    else -> {
                        logger.warn("Unknown config file: $fileName")
                        null
                    }
                }
            }

            // TODO: do something with test mode, read from an additional config?
            return MainTaskTrackerConfig(
                configs = sortConfigHandlers(configs)
            )
        }
    }
}
