package org.jetbrains.research.tasktracker.config.ide

import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionConfig
import org.jetbrains.research.tasktracker.config.ide.settings.SettingsConfig
import org.jetbrains.research.tasktracker.config.util.ConfigUtil
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.handler.ide.IdeHandler
import java.io.File

class MainIdeConfig : BaseConfig {
    var inspectionConfig: InspectionConfig? = null
    var settingsConfig: SettingsConfig? = null

    override fun buildHandler(): BaseHandler = IdeHandler(this)

    companion object {
        fun buildConfig(configFiles: List<File>): MainIdeConfig {
            val mainIdeConfig = MainIdeConfig()
            configFiles.forEach { configFile ->
                when {
                    configFile.name.startsWith(InspectionConfig.CONFIG_FILE_PREFIX) -> {
                        mainIdeConfig.inspectionConfig = ConfigUtil.buildConfig(
                            mainIdeConfig.inspectionConfig,
                            "inspections",
                            configFile,
                            InspectionConfig::buildConfig
                        )
                    }

                    configFile.name.startsWith(SettingsConfig.CONFIG_FILE_PREFIX) -> {
                        mainIdeConfig.settingsConfig = ConfigUtil.buildConfig(
                            mainIdeConfig.settingsConfig,
                            "settingsConfig",
                            configFile,
                            SettingsConfig::buildConfig
                        )
                    }
                }
            }
            return mainIdeConfig
        }
    }
}
