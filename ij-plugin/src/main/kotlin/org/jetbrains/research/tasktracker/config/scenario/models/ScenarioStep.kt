package org.jetbrains.research.tasktracker.config.scenario.models

import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler

@Serializable
data class ScenarioStep(
    private val ideConfig: MainIdeConfig?,
    val units: List<BaseConfig>,
    val mode: ScenarioStepMode = ScenarioStepMode.ORDERED
) {
    @Transient
    private var mainIdeHandler: BaseProjectHandler? = null

    fun prepareSettings(project: Project) {
        mainIdeHandler = ideConfig?.buildHandler(project)
        mainIdeHandler?.setup()
    }

    fun run() {
        TODO()
    }

    // TODO check if there are any config of specified type in units.
    @Suppress("FunctionOnlyReturningConstant")
    fun isValid() = true
}
