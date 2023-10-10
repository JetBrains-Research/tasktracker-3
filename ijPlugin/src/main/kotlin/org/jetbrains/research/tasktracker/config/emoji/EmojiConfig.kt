package org.jetbrains.research.tasktracker.config.emoji

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.handler.emoji.EmojiHandler
import java.io.File

@Serializable
data class EmojiConfig(
    val emojis: List<Emoji>
) : BaseConfig {
    override val configName: String
        get() = "emoji"

    override fun buildHandler(): BaseHandler = EmojiHandler(this)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "emoji"

        fun buildConfig(configFile: File): EmojiConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}