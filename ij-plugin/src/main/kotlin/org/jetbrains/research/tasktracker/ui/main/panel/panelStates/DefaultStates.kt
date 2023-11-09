package org.jetbrains.research.tasktracker.ui.main.panel.panelStates

import com.intellij.openapi.application.ApplicationManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.research.tasktracker.config.content.task.base.Task
import org.jetbrains.research.tasktracker.config.content.task.base.TaskWithFiles
import org.jetbrains.research.tasktracker.requests.FileRequests.send
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
        selectTask()
    }
    setBackAction {
        agreementAcceptance()
    }
}

// TODO refactor it for many configs
/**
 * Switches the panel to the task selection window.
 */
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
private fun Panel.processTask(name: String) {
    startTracking()
    // TODO: change to task by id
    val task =
        MainPanelStorage.taskIdTask.values.find { it.name == name } ?: error("Can't find task with name '$name'")
    ApplicationManager.getApplication().invokeAndWait {
        TaskFileHandler.initTask(project, task)
    }
    (task as? TaskWithFiles)?.focusFileId?.let { id ->
        focusOnfFileById(task, id)
    }
    solveTask(task)
}

/**
 * Switches the panel to the task solving window.
 * It contains task name, description and I/O data.
 */
private fun Panel.solveTask(task: Task) {
    val activityTracker = ActivityTracker(project)
    activityTracker.startTracking()
    loadBasePage(
        SolvePageTemplate(task), "ui.button.submit", true
    )
    setBackAction {
        TaskFileHandler.disposeTask(project, task)
        mainWindow.removeHandlers()
        selectTask()
        activityTracker.stopTracking()
    }
    setNextAction {
        TaskFileHandler.disposeTask(project, task)
        mainWindow.removeHandlers()
        welcomePage()
        activityTracker.stopTracking()
    }
    listenFileRedirection(task)
}

private fun Panel.survey() {
    loadBasePage(
        SurveyTemplate.loadCurrentTemplate(), "ui.button.submit", true
    )
    setNextAction {
        val surveyParser = SurveyParser(mainWindow, project)
        // TODO: rewrite
        GlobalScope.launch {
            surveyParser.parseAndLog()
            // TODO: unify
            val isSuccessful = surveyParser.send()
            if (!isSuccessful) {
                serverErrorPage()
            }
            trackers.forEach {
                it.stopTracking()
            }
            trackers.forEach {
                if (!it.send()) {
                    serverErrorPage()
                }
            }
            trackers.clear()
        }
    }
}

fun Panel.serverErrorPage() {
    trackers.clear()
    loadBasePage(
        ServerErrorPage(), "ui.button.welcome", false, isVisibleNextButton = false
    )
}
