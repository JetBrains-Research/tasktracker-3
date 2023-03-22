package org.jetBrains.research.tasktracker.handler.tracking

import org.jetBrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.jetBrains.research.tasktracker.handler.BaseHandler

class CodeTrackingHandler(override val config: CodeTrackingConfig) : BaseHandler {
    override fun setup() {
        TODO(
            "Setup code tracking according to the config: " +
                "create files for tasks, add listeners for them and for filesToTrack, etc"
        )
    }
}
