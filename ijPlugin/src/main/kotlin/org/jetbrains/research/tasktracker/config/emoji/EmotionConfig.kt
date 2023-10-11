package org.jetbrains.research.tasktracker.config.emoji

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.handler.emoji.EmotionHandler
import java.io.File

@Serializable
data class EmotionConfig(
    val emotions: List<Emotion>
) : BaseConfig {
    override val configName: String
        get() = "emotion"

    override fun buildHandler(): BaseHandler = EmotionHandler(this)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "emotion"

        fun buildConfig(configFile: File): EmotionConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
