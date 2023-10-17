package org.jetbrains.research.tasktracker.handler.tracking

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.tracking.WebCamTrackingConfig
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler

class WebCamTrackingHandler(
    override val config: WebCamTrackingConfig,
    override val project: Project
) : BaseProjectHandler {
    override fun setup() {
//        TODO(
//            "Setup webcam tracking according to the config"
//        )
    }
}
