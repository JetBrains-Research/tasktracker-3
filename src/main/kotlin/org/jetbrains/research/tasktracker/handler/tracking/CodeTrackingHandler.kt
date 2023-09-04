package org.jetbrains.research.tasktracker.handler.tracking

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler

class CodeTrackingHandler(override val config: CodeTrackingConfig) : BaseHandler {
    override fun setup(project: Project) {
//        TODO(
//            "Setup code tracking according to the config: " +
//                "create files for tasks, add listeners for them and for filesToTrack, etc"
//        )
    }
}
