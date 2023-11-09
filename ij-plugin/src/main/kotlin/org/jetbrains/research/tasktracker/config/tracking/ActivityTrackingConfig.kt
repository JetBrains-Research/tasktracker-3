package org.jetbrains.research.tasktracker.config.tracking

import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseProjectConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.tracking.ActivityTrackingHandler
import java.io.File

@Serializable
data class ActivityTrackingConfig(
    override val trackingDeltaSec: Double = BaseTrackingConfig.DEFAULT_TRACKING_DELTA,
) : BaseTrackingConfig, BaseProjectConfig {

    override val configName: String
        get() = "activity_tracking"

    override fun buildHandler(project: Project) = ActivityTrackingHandler(this, project)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "activity_tracking"

        fun buildConfig(configFile: File): ActivityTrackingConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
