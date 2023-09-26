package org.jetbrains.research.tasktracker.util.survey

import com.intellij.openapi.project.Project
import org.jetbrains.concurrency.await
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.survey.*
import org.jetbrains.research.tasktracker.ui.main.panel.MainPluginWindow

class SurveyParser(private val mainWindow: MainPluginWindow, project: Project) {
    val surveyLogger = SurveyLogger(project)

    private val items = TaskTrackerPlugin.mainConfig.surveyConfig?.htmlQuestions ?: emptyList()

    suspend fun parseAndLog() = items.forEachIndexed { index, surveyItem -> parseAndLog(surveyItem, index) }

    private suspend fun parseAndLog(item: HtmlQuestion, id: Int) {
        when (item) {
            is InputHtmlQuestion -> {
                val result = mainWindow.getElementValue(item.elementId).await()
                surveyLogger.log(item.text, result.toString(), questionId = id)
            }

            is TextAreaHtmlQuestion -> {
                val result = mainWindow.getElementValue(item.elementId).await()
                surveyLogger.log(item.text, result.toString(), questionId = id)
            }

            is RadioHtmlQuestion -> item.infos.forEach { info ->
                val result = mainWindow.checkIfRadioButtonChecked(info.id).await()
                surveyLogger.log(item.text, result.toString(), option = info.id, questionId = id)
            }

            is HtmlQuestionContainer -> item.subQuestions.forEach {
                parseAndLog(it, id)
            }
        }
    }
}
