package org.jetbrains.research.tasktracker.util.survey

import com.intellij.openapi.project.Project
import org.jetbrains.concurrency.await
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.survey.InputType
import org.jetbrains.research.tasktracker.config.survey.SurveyItem
import org.jetbrains.research.tasktracker.ui.main.panel.MainPluginWindow

// TODO: move somewhere and add a config + generate a survey by config
class SurveyParser(private val mainWindow: MainPluginWindow, project: Project) {
    val surveyLogger = SurveyLogger(project)

    private val items = TaskTrackerPlugin.mainConfig.surveyConfig?.surveyItems ?: emptyList()

    suspend fun parseAndLog() = items.forEachIndexed { index, surveyItem -> parseAndLog(surveyItem, index) }

    private suspend fun parseAndLog(item: SurveyItem, id: Int) {
        when (item.inputType) {
            InputType.Simple, InputType.TextArea -> {
                val result = mainWindow.getElementValue(item.elementId).await()
                surveyLogger.log(item.question, result.toString(), questionId = id)
            }

            InputType.Radio -> item.subtypes?.forEach { radioItem ->
                val result = mainWindow.checkIfRadioButtonChecked(radioItem.elementId).await()
                surveyLogger.log(item.question, result.toString(), option = radioItem.elementId, questionId = id)
            }
        }
    }
}
