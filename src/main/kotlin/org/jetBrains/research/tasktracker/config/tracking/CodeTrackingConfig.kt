package org.jetBrains.research.tasktracker.config.tracking

import kotlinx.serialization.Serializable
import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetBrains.research.tasktracker.config.tracking.BaseTrackingConfig.Companion.DEFAULT_TRACKING_DELTA
import java.io.File

enum class CodeTrackingGranularity {
    ALL_CHANGES,
    EACH_LINE,
    EACH_FUNCTION
}

@Serializable
data class CodeTrackingConfig(
    override val trackingDeltaSec: Double = DEFAULT_TRACKING_DELTA,
    val granularity: CodeTrackingGranularity = CodeTrackingGranularity.ALL_CHANGES,
    // Additional files to track
    val filesPathToTrack: List<String> = emptyList(),
) : BaseTrackingConfig, BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "code_tracking"

        fun buildConfig(configFile: File): CodeTrackingConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
