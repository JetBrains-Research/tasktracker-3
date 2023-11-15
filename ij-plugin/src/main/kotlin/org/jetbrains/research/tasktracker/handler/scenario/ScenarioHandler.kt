package org.jetbrains.research.tasktracker.handler.scenario

import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.config.scenario.models.ScenarioStep
import org.jetbrains.research.tasktracker.config.scenario.models.ScenarioUnitType

@Suppress("UnusedPrivateMember")
class ScenarioHandler(private val mainConfig: MainTaskTrackerConfig) {
    fun ScenarioStep.run() {
        units.forEach {
            when (it) {
                ScenarioUnitType.IDE_SETTINGS -> {
                    // TODO: handle unit
                }

                ScenarioUnitType.TASK_CONTENT -> {
                    // TODO: handle unit
                }
            }
        }
    }
}
