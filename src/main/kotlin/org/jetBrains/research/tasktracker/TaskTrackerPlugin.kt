package org.jetBrains.research.tasktracker

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_PROPERTIES_FILE
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.pluginFolderPath
import org.jetBrains.research.tasktracker.properties.DataHandler
import org.jetBrains.research.tasktracker.properties.PluginProperties
import org.jetBrains.research.tasktracker.properties.TestMode
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object TaskTrackerPlugin {
    private const val TEST_MODE_PROPERTY_NAME = "test_mode"
    private const val DATA_HANDLER_PROPERTY_NAME = "data_handler"

    private const val CONFIG_ROOT_PROPERTY_NAME = "config_folder_root"

    private val propertiesFile = File("$pluginFolderPath/$PLUGIN_PROPERTIES_FILE")
    private val defaultConfigRoot = File("$pluginFolderPath/configs")

    // TODO: add a settings panel to update properties and the main config
    var mainConfig: MainTaskTrackerConfig

    init {
        setupPlugin()
        val props = loadProps()
        val pluginProps = props.toPluginProperties()
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

    private fun loadProps() = Properties().also { props -> FileInputStream(propertiesFile).use { props.load(it) } }

    private fun setupPlugin() {
        if (!propertiesFile.exists()) {
            createDefaultPropertiesFile()
        }
    }

    private fun createDefaultPropertiesFile() {
        val props = Properties()
        props.setProperty(TEST_MODE_PROPERTY_NAME, TestMode.ON.propValue)
        props.setProperty(DATA_HANDLER_PROPERTY_NAME, DataHandler.LOCAL_FILE.propValue)
        props.store(FileOutputStream(propertiesFile), "$PLUGIN_NAME base properties")
    }

    private fun Properties.toPluginProperties() = PluginProperties(
        testMode = TestMode.convertToTestMode(this.getProperty(TEST_MODE_PROPERTY_NAME)),
        dataHandler = DataHandler.convertToDataHandler(this.getProperty(DATA_HANDLER_PROPERTY_NAME))
    )
}
