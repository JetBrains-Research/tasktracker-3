package org.jetBrains.research.tasktracker.config.tracking

import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.handler.tracking.BaseTrackingHandler
import java.nio.file.Path

data class ActivityTrackingConfig(
    override val trackingDeltaSec: Double = BaseTrackingConfig.DEFAULT_TRACKING_DELTA,
) : BaseTrackingConfig, BaseConfig {
    override val handler: BaseTrackingHandler = TODO("Not yet implemented")

    companion object {
        const val CONFIG_FILE_PREFIX: String = "activity_tracking"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: Path): ActivityTrackingConfig {
            TODO("Not yet implemented")
        }
    }
}
