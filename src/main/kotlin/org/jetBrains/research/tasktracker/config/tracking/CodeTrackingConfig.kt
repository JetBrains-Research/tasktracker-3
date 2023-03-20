package org.jetBrains.research.tasktracker.config.tracking

import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.config.storage.BaseStorageConfig
import org.jetBrains.research.tasktracker.config.tracking.BaseTrackingConfig.Companion.DEFAULT_TRACKING_DELTA
import org.jetBrains.research.tasktracker.handler.tracking.BaseTrackingHandler
import java.nio.file.Path
import kotlin.reflect.KClass

enum class CodeTrackingGranularity {
    ALL_CHANGES,
    EACH_LINE,
    EACH_FUNCTION
}

data class CodeTrackingConfig(
    override val trackingDeltaSec: Double = DEFAULT_TRACKING_DELTA,
    val granularity: CodeTrackingGranularity = CodeTrackingGranularity.ALL_CHANGES,
) : BaseTrackingConfig, BaseConfig {
    override val handler: BaseTrackingHandler = TODO("Not yet implemented")
    override val dependentConfigs: List<KClass<*>>
        get() = listOf(BaseStorageConfig::class)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "code_tracking"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: Path): CodeTrackingConfig {
            TODO("Not yet implemented")
        }
    }
}
