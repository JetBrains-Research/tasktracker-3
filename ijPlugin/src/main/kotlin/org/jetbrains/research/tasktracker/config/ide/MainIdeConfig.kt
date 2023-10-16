package org.jetbrains.research.tasktracker.config.ide

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionConfig
import org.jetbrains.research.tasktracker.config.ide.settings.SettingsConfig
import org.jetbrains.research.tasktracker.config.util.buildBaseConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.handler.ide.IdeHandler
import java.io.File

class MainIdeConfig : BaseConfig {
    var inspectionConfig: InspectionConfig? = null
    var settingsConfig: SettingsConfig? = null
    override val configName: String
        get() = "main_ide"

    override fun buildHandler(): BaseHandler = IdeHandler(this)

    companion object {
        private val logger = Logger.getInstance(MainIdeConfig::class.java)

        fun buildConfig(configFiles: List<File>): MainIdeConfig {
            val mainIdeConfig = MainIdeConfig()
            configFiles.forEach { configFile ->
                when {
                    configFile.name.startsWith(InspectionConfig.CONFIG_FILE_PREFIX) -> {
                        mainIdeConfig.inspectionConfig = buildBaseConfig(
                            configFile,
                            InspectionConfig::buildConfig,
                            logger
                        )
                    }

                    configFile.name.startsWith(SettingsConfig.CONFIG_FILE_PREFIX) -> {
                        mainIdeConfig.settingsConfig = buildBaseConfig(
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
