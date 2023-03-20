package org.jetBrains.research.tasktracker.handler.ide

import org.jetBrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetBrains.research.tasktracker.handler.BaseHandler

class IdeHandler(override val config: MainIdeConfig) : BaseHandler {
    override fun setup() {
        TODO("Setup IDE according to the config")
    }
}
