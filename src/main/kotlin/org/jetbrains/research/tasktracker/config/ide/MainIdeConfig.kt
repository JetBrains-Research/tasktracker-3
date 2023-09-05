package org.jetbrains.research.tasktracker.config.ide

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionConfig
import org.jetbrains.research.tasktracker.config.ide.settings.SettingsConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.handler.ide.IdeHandler
import java.io.File

class MainIdeConfig : BaseConfig {
    var inspectionConfig: InspectionConfig? = null
    var settingsConfig: SettingsConfig? = null

    override fun buildHandler(): BaseHandler = IdeHandler(this)

    companion object {
        private val logger = Logger.getInstance(MainIdeConfig::class.java)

        fun buildConfig(configFiles: List<File>): MainIdeConfig {
            val mainIdeConfig = MainIdeConfig()
            configFiles.forEach { configFile ->
                when {
                    configFile.name.startsWith(InspectionConfig.CONFIG_FILE_PREFIX) -> {
                        require(mainIdeConfig.inspectionConfig == null) {
                            "The inspections config was already parsed"
                        }
                        logger.info("Building config for inspections...")
                        mainIdeConfig.inspectionConfig = InspectionConfig.buildConfig(configFile)
                    }

                    configFile.name.startsWith(SettingsConfig.CONFIG_FILE_PREFIX) -> {
                        require(mainIdeConfig.settingsConfig == null) {
                            "The inspections config was already parsed"
                        }
                        logger.info("Building config for completion...")
                        mainIdeConfig.settingsConfig = SettingsConfig.buildConfig(configFile)
                    }
                }
            }
            return mainIdeConfig
        }
    }
}
