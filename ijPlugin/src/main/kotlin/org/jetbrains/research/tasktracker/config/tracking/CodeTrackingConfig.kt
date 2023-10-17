package org.jetbrains.research.tasktracker.config.tracking

import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseProjectConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.config.tracking.BaseTrackingConfig.Companion.DEFAULT_TRACKING_DELTA
import org.jetbrains.research.tasktracker.handler.tracking.CodeTrackingHandler
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
) : BaseTrackingConfig, BaseProjectConfig {

    override val configName: String
        get() = "code_tracking"

    override fun buildHandler(project: Project) = CodeTrackingHandler(this, project)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "code_tracking"

        fun buildConfig(configFile: File): CodeTrackingConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
