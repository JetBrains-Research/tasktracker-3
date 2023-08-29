package org.jetbrains.research.tasktracker.handler.ide

import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler

class InspectionHandler(override val config: InspectionConfig) : BaseHandler {
    override fun setup() {
        // TODO "Setup inspections according to the config"
    }
}
