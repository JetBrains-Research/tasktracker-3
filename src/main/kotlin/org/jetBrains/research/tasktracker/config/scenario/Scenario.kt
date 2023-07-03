package org.jetBrains.research.tasktracker.config.scenario

import com.intellij.openapi.diagnostic.Logger
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetBrains.research.tasktracker.TaskTrackerPlugin
import org.jetBrains.research.tasktracker.config.BaseConfig
import java.util.*

enum class ScenarioUnitType {
    TASK_CONTENT,

    // TODO: add other types of content
    IDE_SETTINGS;

    val config: BaseConfig?
        get() = when (this) {
            TASK_CONTENT -> TaskTrackerPlugin.mainConfig.taskContentConfig
            IDE_SETTINGS -> TaskTrackerPlugin.mainConfig.mainIdeConfig
        }
}

@Serializable
data class ScenarioStep(
    // TODO: sort according to the plugin's implementation to make sure in the right order
    val units: List<ScenarioUnitType>
) {
    fun isValid() = units.any { it.config != null }
}

@Serializable
data class Scenario(
    val steps: Queue<ScenarioStep>
) {
    @Transient
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
