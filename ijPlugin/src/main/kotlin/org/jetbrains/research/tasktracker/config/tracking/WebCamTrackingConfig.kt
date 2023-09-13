package org.jetbrains.research.tasktracker.config.tracking

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.tracking.WebCamTrackingHandler
import java.io.File

enum class WebCamTrackingGranularity {
    ALWAYS
}

@Serializable
data class WebCamTrackingConfig(
    val granularity: WebCamTrackingGranularity = WebCamTrackingGranularity.ALWAYS,
) : BaseConfig {
    override val configName: String
        get() = "webcam_tracking"

    override fun buildHandler() = WebCamTrackingHandler(this)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "webcam_tracking"

        fun buildConfig(configFile: File): WebCamTrackingConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
