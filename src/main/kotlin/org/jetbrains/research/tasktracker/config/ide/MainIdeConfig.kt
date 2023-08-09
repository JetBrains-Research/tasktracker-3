package org.jetbrains.research.tasktracker.config.ide

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.ide.IdeHandler
import java.io.File

@Serializable
class MainIdeConfig : BaseConfig {

    val test: String = "" // TODO make real fields

    override fun buildHandler() = IdeHandler(this)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "ide"

        fun buildConfig(configFile: File): MainIdeConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
