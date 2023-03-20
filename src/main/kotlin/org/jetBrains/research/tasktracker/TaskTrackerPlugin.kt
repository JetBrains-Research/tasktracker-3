package org.jetBrains.research.tasktracker

import com.intellij.openapi.diagnostic.Logger
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetBrains.research.tasktracker.properties.DataHandler
import org.jetBrains.research.tasktracker.properties.PluginProperties
import org.jetBrains.research.tasktracker.properties.PropertiesController.CONFIG_ROOT_PROPERTY_NAME
import org.jetBrains.research.tasktracker.properties.PropertiesController.defaultConfigRoot
import org.jetBrains.research.tasktracker.properties.PropertiesController.loadProps
import org.jetBrains.research.tasktracker.properties.PropertiesController.toPluginProperties
import java.io.File
import java.util.*

object TaskTrackerPlugin {
    private val logger: Logger = Logger.getInstance(javaClass)

    // TODO: add a settings panel to update properties and the main config
    var mainConfig: MainTaskTrackerConfig

    init {
        val props = loadProps()
        val pluginProps = props.toPluginProperties()
        logger.info("Building the main config...")
        mainConfig = buildConfig(props, pluginProps)
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
}
