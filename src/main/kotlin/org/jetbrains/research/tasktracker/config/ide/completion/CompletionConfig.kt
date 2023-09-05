package org.jetbrains.research.tasktracker.config.ide.completion

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.ide.CompletionHandler
import java.io.File

@Serializable
data class CompletionConfig(val enableCompletion: Boolean) : BaseConfig {

    override fun buildHandler() = CompletionHandler(this)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "completion"

        fun buildConfig(configFile: File): CompletionConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
