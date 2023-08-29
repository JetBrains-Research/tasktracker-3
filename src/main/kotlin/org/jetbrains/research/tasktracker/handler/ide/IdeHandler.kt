package org.jetbrains.research.tasktracker.handler.ide

import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler

class IdeHandler(override val config: MainIdeConfig) : BaseHandler {
    override fun setup() {
        config.inspectionConfig?.buildHandler()
    }
}
