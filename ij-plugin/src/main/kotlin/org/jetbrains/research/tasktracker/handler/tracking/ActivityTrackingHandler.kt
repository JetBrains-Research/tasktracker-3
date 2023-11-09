package org.jetbrains.research.tasktracker.handler.tracking

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.tracking.ActivityTrackingConfig
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler

class ActivityTrackingHandler(
    override val config: ActivityTrackingConfig,
    override val project: Project
) : BaseProjectHandler {
    override fun setup() {
//        TODO(
//            "Setup activity tracking according to the config"
//        )
    }
}
