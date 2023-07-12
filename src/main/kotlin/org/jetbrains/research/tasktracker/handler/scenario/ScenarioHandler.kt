package org.jetbrains.research.tasktracker.handler.scenario

import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.config.scenario.ScenarioStep
import org.jetbrains.research.tasktracker.config.scenario.ScenarioUnitType

@Suppress("UnusedPrivateMember")
class ScenarioHandler(private val mainConfig: MainTaskTrackerConfig) {
    fun ScenarioStep.run() {
        units.forEach {
            when (it) {
                ScenarioUnitType.IDE_SETTINGS -> TODO("Not implemented yet")
                ScenarioUnitType.TASK_CONTENT -> TODO("Not implemented yet")
            }
        }
    }
}
