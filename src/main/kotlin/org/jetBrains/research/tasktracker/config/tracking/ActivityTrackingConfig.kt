package org.jetBrains.research.tasktracker.config.tracking

import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.handler.tracking.BaseTrackingHandler
import java.io.File

data class ActivityTrackingConfig(
    override val trackingDeltaSec: Double = BaseTrackingConfig.DEFAULT_TRACKING_DELTA,
) : BaseTrackingConfig, BaseConfig {
    override val handler: BaseTrackingHandler = TODO("Not implemented yet")

    companion object {
        const val CONFIG_FILE_PREFIX: String = "activity_tracking"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: File): ActivityTrackingConfig {
            TODO("Not implemented yet")
        }
    }
}
