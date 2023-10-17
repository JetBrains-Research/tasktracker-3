package org.jetbrains.research.tasktracker.config.tracking

import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseProjectConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.tracking.WebCamTrackingHandler
import java.io.File

enum class WebCamTrackingGranularity {
    ALWAYS
}

@Serializable
data class WebCamTrackingConfig(
    override val trackingDeltaSec: Double = BaseTrackingConfig.DEFAULT_TRACKING_DELTA,
    val granularity: WebCamTrackingGranularity = WebCamTrackingGranularity.ALWAYS,
) : BaseProjectConfig, BaseTrackingConfig {
    override val configName: String
        get() = "webcam_tracking"

    override fun buildHandler(project: Project) = WebCamTrackingHandler(this, project)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "webcam_tracking"

        fun buildConfig(configFile: File): WebCamTrackingConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
