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
        checkInputs()
            .onSuccess {
                if (!it) {
                    welcomePage()
                } else {
                    notifyError(project, UIBundle.message("ui.please.fill"))
                }
            }
            .onError {
                error(it.localizedMessage)
            }
    }
}

/**
 * Switches the panel to the plugin description window.
 */
fun Panel.welcomePage() {
    loadBasePage(MainPageTemplate.loadCurrentTemplate(), "ui.button.next", true)
    setNextAction {
        processScenario()
    }
}

// TODO refactor it for many configs
/**
 * Switches the panel to the task selection window.
 */
@Suppress("UnusedPrivateMember")
private fun Panel.selectTask() {
    loadBasePage(
        TasksPageTemplate(MainPanelStorage.taskIdTask.values.toList()), "ui.button.select", true
    )
    setNextAction {
        mainWindow.getElementValue("tasks").onSuccess { name ->
            processTask(name.toString())
        }.onError {
            error(it.localizedMessage)
        }
    }
    setBackAction {
        welcomePage()
    }
}

/**
 * Loads configs by selected task and language
 */
fun Panel.processTask(id: String) {
    startTracking() // TODO
    val task =
        MainPanelStorage.taskIdTask.values.find { it.id == id } ?: error("Can't find task with id '$id'")
    ApplicationManager.getApplication().invokeAndWait {
        TaskFileHandler.initTask(project, task)
    }
    (task as? TaskWithFiles)?.focusFileId?.let { fileId ->
        focusOnfFileById(task, fileId)
    }
    solveTask(task)
}

/**
 * Switches the panel to the task solving window.
 * It contains task name, description and I/O data.
 */
private fun Panel.solveTask(task: Task) {
    val activityTracker = ActivityTracker(project)
    activityTracker.startTracking() // TODO
    loadBasePage(SolvePageTemplate(task))
    setNextAction {
        TaskFileHandler.disposeTask(project, task)
        processScenario()
    }
    listenFileRedirection(task)
}

@OptIn(DelicateCoroutinesApi::class)
fun Panel.survey() {
    loadBasePage(SurveyTemplate.loadCurrentTemplate())
    setNextAction {
        val surveyParser = SurveyParser(mainWindow, project)
        GlobalScope.launch {
            surveyParser.parseAndLog()
        }
        processScenario()
    }
}

fun Panel.serverErrorPage() {
    trackers.clear()
    loadBasePage(
        ServerErrorPage(), "ui.button.welcome", false, isVisibleNextButton = false
    )
}

fun Panel.processScenario() {
    val scenario =
        TaskTrackerPlugin.mainConfig.scenarioConfig?.scenario
            ?: error("Unexpected error, Scenario config must exist!")
    when (val unit = scenario.getNextUnit(project)) {
        is TaskListUnit -> {
            TODO()
        }

        is TaskListWithSingleChoiceUnit -> {
            TODO()
        }

        is TaskUnit -> {
            processTask(unit.id)
        }

        is IdeSettingUnit -> {
            unit.mainIdeConfig.buildHandler(project).also {
                MainPanelStorage.activeIdeHandlers.addFirst(it)
                it.setup()
            }
            processScenario()
        }

        is SurveyUnit -> {
            survey()
        }

        is ExternalSourceUnit -> {
            TODO()
        }

        null -> {
            TODO()
        }
    }
}
