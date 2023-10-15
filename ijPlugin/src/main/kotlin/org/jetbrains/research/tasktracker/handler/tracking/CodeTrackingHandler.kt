package org.jetbrains.research.tasktracker.handler.tracking

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler

class CodeTrackingHandler(override val config: CodeTrackingConfig, override val project: Project) : BaseProjectHandler {
    override fun setup() {
//        TODO(
//            "Setup code tracking according to the config: " +
//                "create files for tasks, add listeners for them and for filesToTrack, etc"
//        )
    }
}
