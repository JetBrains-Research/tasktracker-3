package org.jetBrains.research.tasktracker.config.tracking

// To be consistent between code tracking and activity tracking changes
interface BaseTrackingConfig {
    val trackingDeltaSec: Double

    companion object {
        const val DEFAULT_TRACKING_DELTA = 0.4
    }
}
