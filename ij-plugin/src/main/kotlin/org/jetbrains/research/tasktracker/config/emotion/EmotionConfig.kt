package org.jetbrains.research.tasktracker.config.emotion

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.handler.emoji.EmotionHandler
import java.io.File

@Serializable
data class EmotionConfig(
    /**
     * All available emotions for tracking
     */
    private val emotions: List<Emotion>,
    /**
     * .onnx model filename
     */
    val modelFilename: String,
    /**
     * Input point name in onnx model
     */
    val modelInputGate: String,
    /**
     * Output point name in onnx model
     */
    val modelOutputGate: String
) : BaseConfig {
    override val configName: String
        get() = "emotion"

    override fun buildHandler(): BaseHandler = EmotionHandler(this)

    @Transient
    private val modelPositionToEmotion = emotions.associateBy { it.modelPosition }

    @Transient
    val modelPositionToThreshold =
        emotions.filter { it.modelPosition >= 0 }.associate { it.modelPosition to it.threshold }

    fun getEmotion(modelPosition: Int) = modelPositionToEmotion[modelPosition]

    companion object {
        const val CONFIG_FILE_PREFIX: String = "emotion"

        fun buildConfig(configFile: File): EmotionConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
