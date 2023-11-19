package org.jetbrains.research.tasktracker.config.scenario.models

import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage

@Serializable
data class ScenarioStep(
    private val units: List<ScenarioUnit>,
    private val ideConfig: MainIdeConfig? = null,
    val mode: ScenarioStepMode = ScenarioStepMode.ORDERED
) {

    @Transient
    private var mainIdeHandler: BaseProjectHandler? = null

    fun prepareSettings(project: Project) {
        mainIdeHandler = ideConfig?.buildHandler(project)
        mainIdeHandler?.let {
            MainPanelStorage.activeIdeHandlers.addFirst(it)
            it.setup()
        }
    }

    fun getUnits() = when (mode) {
        ScenarioStepMode.ORDERED -> units
        ScenarioStepMode.SHUFFLED -> units.shuffled()
    }

    // TODO check if there are any config of specified type in units.
    @Suppress("FunctionOnlyReturningConstant")
    fun isValid() = true
}
