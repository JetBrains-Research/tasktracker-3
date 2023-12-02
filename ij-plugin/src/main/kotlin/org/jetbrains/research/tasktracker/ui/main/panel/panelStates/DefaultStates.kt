package org.jetbrains.research.tasktracker.ui.main.panel.panelStates

import com.intellij.openapi.application.ApplicationManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.content.task.base.Task
import org.jetbrains.research.tasktracker.config.content.task.base.TaskWithFiles
import org.jetbrains.research.tasktracker.config.scenario.models.*
import org.jetbrains.research.tasktracker.tracking.TaskFileHandler
import org.jetbrains.research.tasktracker.tracking.activity.ActivityTracker
import org.jetbrains.research.tasktracker.ui.main.panel.MainPluginPanelFactory
import org.jetbrains.research.tasktracker.ui.main.panel.runOnSuccess
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage
import org.jetbrains.research.tasktracker.ui.main.panel.template.*
import org.jetbrains.research.tasktracker.util.UIBundle
import org.jetbrains.research.tasktracker.util.notifier.notifyError
import org.jetbrains.research.tasktracker.util.survey.SurveyParser

typealias Panel = MainPluginPanelFactory

/**
 * A page for collecting user data, and checkboxes for user agreement acceptance.
 */
fun Panel.agreementAcceptance() {
    loadBasePage(AgreementTemplate.loadCurrentTemplate(), "ui.button.next", false)
    setNextAction {
        checkAgreementInputs().runOnSuccess {
            if (!it) {
                welcomePage()
            } else {
                notifyError(project, UIBundle.message("ui.please.fill"))
            }
        }
    }
}

/**
 * Switches the panel to the plugin description window.
 */
fun Panel.welcomePage() {
    loadBasePage(MainPageTemplate.loadCurrentTemplate(), "ui.button.next", false)
    setNextAction {
        processScenario()
    }
}

// TODO refactor it for many configs
/**
 * Switches the panel to the task selection window.
 */
@Suppress("UnusedPrivateMember")
private fun Panel.selectTask(taskIds: List<String>, allRequired: Boolean = true) {
    val tasks = TaskTrackerPlugin.mainConfig.taskContentConfig?.tasks?.filter { it.id in taskIds } ?: emptyList()
    loadBasePage(TasksPageTemplate(tasks))
    setNextAction {
        mainWindow.getElementValue("tasks").runOnSuccess { name ->
            solveTask(name.toString(), if (allRequired) taskIds.filter { it != name } else emptyList())
        }
    }
}

/**
 * Loads configs by selected task and language
 */
fun Panel.processTask(id: String): Task {
    startTracking() // TODO
    val task =
        MainPanelStorage.taskIdTask.values.find { it.id == id } ?: error("Can't find task with id '$id'")
    ApplicationManager.getApplication().invokeAndWait {
        TaskFileHandler.initTask(project, task)
    }
    (task as? TaskWithFiles)?.focusFileId?.let { fileId ->
        focusOnfFileById(task, fileId)
    }
    return task
}

/**
 * Switches the panel to the task solving window.
 * It contains task name, description and I/O data.
 */
private fun Panel.solveTask(id: String, nextTasks: List<String> = emptyList()) {
    val task = processTask(id)
    val activityTracker = ActivityTracker(project)
    activityTracker.startTracking() // TODO start tracking for all trackers instead of this one
    loadBasePage(SolvePageTemplate(task))
    setNextAction {
        TaskFileHandler.disposeTask(project, task)
        if (nextTasks.isNotEmpty()) {
            selectTask(nextTasks)
        } else {
            processScenario()
        }
    }
    listenFileRedirection(task)
}

@OptIn(DelicateCoroutinesApi::class)
fun Panel.survey(id: String) {
    val survey = TaskTrackerPlugin.mainConfig.surveyConfig?.surveys?.find { it.id == id }
        ?: error("Survey with id `$id` hasn't been found.")
    loadBasePage(SurveyTemplate(survey))
    setNextAction {
        checkSurveyInputs().runOnSuccess {
            if (it) {
                val surveyParser = SurveyParser(mainWindow, project)
                GlobalScope.launch {
                    surveyParser.parseAndLog(survey)
                }
                processScenario()
            } else {
                notifyError(project, UIBundle.message("ui.please.fill"))
            }
        }
    }
}

fun Panel.serverErrorPage() {
    trackers.clear()
    loadBasePage(ServerErrorPage(), "ui.button.welcome", false)
}

fun Panel.finalPage() {
    loadBasePage(FinalPageTemplate.loadCurrentTemplate(), "ui.button.welcome", false)
    setNextAction {
        welcomePage()
    }
}

fun Panel.processScenario() {
    val scenario =
        TaskTrackerPlugin.mainConfig.scenarioConfig?.scenario
            ?: error("Unexpected error, Scenario config must exist!")
    when (val unit = scenario.getNextUnit(project)) {
        is TaskListUnit -> {
            selectTask(unit.taskIds)
        }

        is TaskListWithSingleChoiceUnit -> {
            selectTask(unit.taskIds, allRequired = false)
        }

        is TaskUnit -> {
            solveTask(unit.id)
        }

        is IdeSettingUnit -> {
            unit.mainIdeConfig.buildHandler(project).also {
                MainPanelStorage.activeIdeHandlers.addFirst(it)
                it.setup()
            }
            processScenario()
        }

        is SurveyUnit -> {
            survey(unit.id)
        }

        is ExternalSourceUnit -> {
            openExternalUrl(unit.url)
        }

        null -> {
            scenario.reset()
            finalPage()
        }
    }
}
