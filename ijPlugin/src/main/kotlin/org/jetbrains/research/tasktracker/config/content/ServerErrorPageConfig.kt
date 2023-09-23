package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

/**
 * default message template is a field which is representing template
 * in which we will paste link to the local directory with saved files.
 */
@Serializable
class ServerErrorPageConfig(private val defaultMessageTemplate: String) : BaseConfig {
    override val configName: String
        get() = CONFIG_FILE_PREFIX

    val defaultMessage: String
            get() = defaultMessageTemplate.format(MainTaskTrackerConfig.pluginFolderPath)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "server_error"

        fun buildConfig(configFile: File): ServerErrorPageConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
