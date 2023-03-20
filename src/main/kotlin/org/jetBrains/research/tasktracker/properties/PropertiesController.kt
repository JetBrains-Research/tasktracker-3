package org.jetBrains.research.tasktracker.properties

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object PropertiesController {
    private const val TEST_MODE_PROPERTY_NAME = "test_mode"
    private const val DATA_HANDLER_PROPERTY_NAME = "data_handler"

    const val CONFIG_ROOT_PROPERTY_NAME = "config_folder_root"

    private val propertiesFile =
        File("${MainTaskTrackerConfig.pluginFolderPath}/${MainTaskTrackerConfig.PLUGIN_PROPERTIES_FILE}")
    val defaultConfigRoot = File("${MainTaskTrackerConfig.pluginFolderPath}/configs")

    fun loadProps() = Properties().also { props ->
        createPropertiesFile()
        FileInputStream(propertiesFile).use { props.load(it) }
    }

    private fun createPropertiesFile() {
        if (!propertiesFile.exists()) {
            createDefaultPropertiesFile()
        }
    }

    private fun createDefaultPropertiesFile() {
        val props = Properties()
        props.setProperty(TEST_MODE_PROPERTY_NAME, TestMode.ON.propValue)
        props.setProperty(DATA_HANDLER_PROPERTY_NAME, DataHandler.LOCAL_FILE.propValue)
        props.store(FileOutputStream(propertiesFile), "${MainTaskTrackerConfig.PLUGIN_NAME} base properties")
    }

    fun Properties.toPluginProperties() = PluginProperties(
        testMode = TestMode.convertToTestMode(this.getProperty(TEST_MODE_PROPERTY_NAME)),
        dataHandler = DataHandler.convertToDataHandler(this.getProperty(DATA_HANDLER_PROPERTY_NAME))
    )
}
