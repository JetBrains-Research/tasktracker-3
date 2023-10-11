package org.jetbrains.research.tasktracker.config

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.content.FinalPageContentConfig
import org.jetbrains.research.tasktracker.config.content.MainPageContentConfig
import org.jetbrains.research.tasktracker.config.content.ServerErrorPageConfig
import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.config.emoji.EmotionConfig
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.scenario.ScenarioConfig
import org.jetbrains.research.tasktracker.config.survey.SurveyConfig
import org.jetbrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetbrains.research.tasktracker.config.tracking.WebCamTrackingConfig
import org.jetbrains.research.tasktracker.config.util.buildBaseConfig
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
    var webCamConfig: WebCamTrackingConfig? = null,
    var mainPageConfig: MainPageContentConfig? = null,
    var finalPageConfig: FinalPageContentConfig? = null,
    var serverErrorPageConfig: ServerErrorPageConfig? = null,
    var emotionConfig: EmotionConfig? = null,

    var scenarioConfig: ScenarioConfig? = null,
    var surveyConfig: SurveyConfig? = null,
) {
    fun getAllConfigs() = listOf(
        taskContentConfig,
        mainIdeConfig,
        activityTrackingConfig,
        codeTrackingConfig,
        scenarioConfig,
        webCamConfig,
        mainPageConfig,
        finalPageConfig,
        serverErrorPageConfig,
        emotionConfig
    )

    companion object {
        val logger = Logger.getInstance(MainTaskTrackerConfig::class.java)

        const val PLUGIN_NAME = "tasktracker"
        val pluginFolderPath = "${PathManager.getPluginsPath()}/$PLUGIN_NAME"
        val logFilesFolder = "$pluginFolderPath/logs"
        const val PLUGIN_PROPERTIES_FILE = "$PLUGIN_NAME.properties"

        private fun File.isConfigFile() = this.extension == "yaml"

        // TODO: add a builder for server-based properties
        @Suppress("LongMethod")
        fun buildConfig(pluginProperties: PluginProperties, configRoot: File): MainTaskTrackerConfig {
            val mainConfig = MainTaskTrackerConfig(pluginProperties)

            val configFiles = configRoot.walkTopDown().filter { it.isConfigFile() }.toList()
            require(configFiles.isNotEmpty()) { "The root directory with config files must not be empty!" }
            configFiles.forEach { configFile ->
                val fileName = configFile.name
                when {
                    fileName.startsWith(CodeTrackingConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.codeTrackingConfig = buildBaseConfig(
                            mainConfig.codeTrackingConfig, configFile, CodeTrackingConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(ActivityTrackingConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.activityTrackingConfig = buildBaseConfig(
                            mainConfig.activityTrackingConfig, configFile, ActivityTrackingConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(TaskContentConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.taskContentConfig = buildBaseConfig(
                            mainConfig.taskContentConfig, configFile, TaskContentConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(ScenarioConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.scenarioConfig = buildBaseConfig(
                            mainConfig.scenarioConfig, configFile, ScenarioConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(WebCamTrackingConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.webCamConfig = buildBaseConfig(
                            mainConfig.webCamConfig, configFile, WebCamTrackingConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(MainPageContentConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.mainPageConfig = buildBaseConfig(
                            mainConfig.mainPageConfig, configFile, MainPageContentConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(FinalPageContentConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.finalPageConfig = buildBaseConfig(
                            mainConfig.finalPageConfig, configFile, FinalPageContentConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(ServerErrorPageConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.serverErrorPageConfig = buildBaseConfig(
                            mainConfig.serverErrorPageConfig, configFile, ServerErrorPageConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(SurveyConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.surveyConfig = buildBaseConfig(
                            mainConfig.surveyConfig, configFile, SurveyConfig::buildConfig, logger
                        )
                    }

                    fileName.startsWith(EmotionConfig.CONFIG_FILE_PREFIX) -> {
                        mainConfig.emotionConfig = buildBaseConfig(
                            mainConfig.emotionConfig, configFile, EmotionConfig::buildConfig, logger
                        )
                    }
                }
            }
            mainConfig.mainIdeConfig = MainIdeConfig.buildConfig(configFiles)
            return mainConfig
        }
    }
}
