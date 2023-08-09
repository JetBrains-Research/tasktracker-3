package org.jetbrains.research.tasktracker.config.tracking

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.tracking.ActivityTrackingHandler
import java.io.File

@Serializable
data class ActivityTrackingConfig(
    override val trackingDeltaSec: Double = BaseTrackingConfig.DEFAULT_TRACKING_DELTA,
) : BaseTrackingConfig, BaseConfig {

    override fun buildHandler() = ActivityTrackingHandler(this)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "activity_tracking"

        fun buildConfig(configFile: File): ActivityTrackingConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
