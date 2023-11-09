package org.jetbrains.research.tasktracker.config.ide

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.BaseProjectConfig
import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionConfig
import org.jetbrains.research.tasktracker.config.ide.settings.SettingsConfig
import org.jetbrains.research.tasktracker.config.util.buildBaseConfig
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler
import org.jetbrains.research.tasktracker.handler.ide.IdeHandler
import java.io.File

class MainIdeConfig : BaseProjectConfig {
    var inspectionConfig: InspectionConfig? = null
    var settingsConfig: SettingsConfig? = null
    override val configName: String
        get() = "main_ide"

    override fun buildHandler(project: Project): BaseProjectHandler = IdeHandler(this, project)

    companion object {
        private val logger = Logger.getInstance(MainIdeConfig::class.java)

        fun buildConfig(configFiles: List<File>): MainIdeConfig {
            val mainIdeConfig = MainIdeConfig()
            configFiles.forEach { configFile ->
                when {
                    configFile.name.startsWith(InspectionConfig.CONFIG_FILE_PREFIX) -> {
                        mainIdeConfig.inspectionConfig = buildBaseConfig(
                            mainIdeConfig.inspectionConfig,
                            configFile,
                            InspectionConfig::buildConfig,
                            logger
                        )
                    }

                    configFile.name.startsWith(SettingsConfig.CONFIG_FILE_PREFIX) -> {
                        mainIdeConfig.settingsConfig = buildBaseConfig(
                            mainIdeConfig.settingsConfig,
                            configFile,
                            SettingsConfig::buildConfig,
                            logger
                        )
                    }
                }
            }
            return mainIdeConfig
        }
    }
}
