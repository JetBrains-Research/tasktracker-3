package org.jetbrains.research.tasktracker

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.DefaultConfigsFactory
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.handler.init.InitializationHandler
import org.jetbrains.research.tasktracker.properties.DataHandler
import org.jetbrains.research.tasktracker.properties.PluginProperties
import org.jetbrains.research.tasktracker.properties.PropertiesController.CONFIG_ROOT_PROPERTY_NAME
import org.jetbrains.research.tasktracker.properties.PropertiesController.defaultConfigRoot
import org.jetbrains.research.tasktracker.properties.PropertiesController.loadProps
import org.jetbrains.research.tasktracker.properties.PropertiesController.toPluginProperties
import org.jetbrains.research.tasktracker.properties.TestMode
import java.io.File
import java.util.*

object TaskTrackerPlugin {
    private val logger: Logger = Logger.getInstance(javaClass)

    // TODO: add a settings panel to update properties and the main config
    lateinit var mainConfig: MainTaskTrackerConfig
    lateinit var initializationHandler: InitializationHandler

    fun initPlugin() {
        initPluginFolder()
        val props = loadProps()
        val pluginProps = props.toPluginProperties()
        if (pluginProps.testMode == TestMode.ON) {
            DefaultConfigsFactory.createDefaultConfigs()
        }
        logger.info("Building the main config...")
        mainConfig = buildConfig(props, pluginProps)
        initializationHandler = InitializationHandler(mainConfig)
    }

    private fun buildConfig(props: Properties, pluginProps: PluginProperties): MainTaskTrackerConfig {
        return when (pluginProps.dataHandler) {
            DataHandler.LOCAL_FILE -> {
                val configFolderRoot =
                    props.getProperty(CONFIG_ROOT_PROPERTY_NAME)?.let { File(it) } ?: defaultConfigRoot
                MainTaskTrackerConfig.buildConfig(pluginProps, configFolderRoot)
            }

            DataHandler.SERVER_CONNECTION -> {
                TODO("Get the server values (port, host) from properties")
            }
        }
    }

    fun initPluginFolder() {
        File(MainTaskTrackerConfig.logFilesFolder).mkdirs()
    }
}
