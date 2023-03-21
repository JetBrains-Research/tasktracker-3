package org.jetBrains.research.tasktracker.config.scenario

import com.intellij.openapi.diagnostic.Logger
import org.jetBrains.research.tasktracker.TaskTrackerPlugin
import org.jetBrains.research.tasktracker.config.BaseConfig
import java.util.*

enum class ScenarioUnitType(val config: BaseConfig?) {
    TASK_CONTENT(TaskTrackerPlugin.mainConfig.taskContentConfig),

    // TODO: add other types of content
    IDE_SETTINGS(TaskTrackerPlugin.mainConfig.mainIdeConfig),
}

data class ScenarioStep(
    // TODO: sort according to the plugin's implementation to make sure in the right order
    val units: List<ScenarioUnitType>
) {
    fun isValid() = units.any { it.config != null }
}

data class Scenario(
    val steps: Queue<ScenarioStep>
) {
    private val logger: Logger = Logger.getInstance(javaClass)
    fun getNextStep(): ScenarioStep? {
        var isValid: Boolean
        var step: ScenarioStep
        do {
            step = steps.poll()
            isValid = step.isValid()
            if (!isValid) {
                logger.warn("Found useless step, all configs are null. Skip it")
            }
            if (steps.isEmpty()) {
                logger.warn("No steps found!")
                return null
            }
        } while (!isValid)
        return step
    }
}
