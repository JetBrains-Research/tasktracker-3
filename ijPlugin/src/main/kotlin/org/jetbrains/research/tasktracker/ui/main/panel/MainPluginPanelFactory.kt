package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Modal
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.jcef.JBCefApp
import com.intellij.util.ui.JBUI
import org.jetbrains.concurrency.Promise
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.content.task.base.Task
import org.jetbrains.research.tasktracker.modelInference.model.EmoModel
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.TaskFileHandler
import org.jetbrains.research.tasktracker.tracking.activity.ActivityTracker
import org.jetbrains.research.tasktracker.tracking.fileEditor.FileEditorTracker
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowTracker
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamTracker
import org.jetbrains.research.tasktracker.tracking.webcam.collectAllDevices
import org.jetbrains.research.tasktracker.ui.main.panel.models.ButtonState
import org.jetbrains.research.tasktracker.ui.main.panel.models.LinkType
import org.jetbrains.research.tasktracker.ui.main.panel.panelStates.agreementAcceptance
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import org.jetbrains.research.tasktracker.ui.main.panel.template.HtmlTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.WebcamChoicePageTemplate
import org.jetbrains.research.tasktracker.util.UIBundle
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionListener

/**
 * The class is intended to manage JCEF and Swing components simultaneously,
 * and its methods primarily serve as a way to switch between frames.
 *
 * Note: This class requires JBCefApp to be supported for proper functioning.
 *
 */
class MainPluginPanelFactory : ToolWindowFactory {
    // TODO: init in other place, states can be saved between sessions
    private val nextButton = createJButton("ui.button.next")
    private val backButton = createJButton("ui.button.back", isVisibleProp = false)
    private val logger: Logger = Logger.getInstance(MainPluginPanelFactory::class.java)

    val trackers: MutableList<BaseTracker> = mutableListOf()

    lateinit var mainWindow: MainPluginWindow
    lateinit var project: Project

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.project = project
        mainWindow = project.getService(MainWindowService::class.java).mainWindow
        mainWindow.jComponent.size = JBUI.size(toolWindow.component.width, toolWindow.component.height)
        agreementAcceptance()
        val buttonPanel = JBPanel<JBPanel<*>>(FlowLayout()).apply {
            add(backButton)
            add(nextButton)
        }
        val panel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            add(mainWindow.jComponent)
            add(buttonPanel, BorderLayout.SOUTH)
        }
        toolWindow.component.add(panel)
        listenRedirection()
    }

    override fun isApplicable(project: Project) = super.isApplicable(project) && JBCefApp.isSupported()

    fun startTracking() {
        if (trackers.isNotEmpty()) { // Otherwise we can lose data
            return
        }
        TaskTrackerPlugin.mainConfig.emotionConfig?.let {
            GlobalPluginStorage.emoPredictor = EmoModel(it)
        } ?: error("emotion config must exist by this moment")

        trackers.addAll(
            listOf(
                ActivityTracker(project),
                ToolWindowTracker(project),
                FileEditorTracker(project)
            )
        )
        GlobalPluginStorage.emoPredictor?.let {
            trackers.add(WebCamTracker(project, it))
        }
        trackers.forEach { it.startTracking() }
    }

    fun loadBasePage(
        template: HtmlTemplate,
        buttonTextKey: String,
        isVisibleBackButton: Boolean = true,
        backButtonText: String? = null,
        isVisibleNextButton: Boolean = true
    ) {
        mainWindow.loadHtmlTemplate(template)
        nextButton.text = UIBundle.message(buttonTextKey)
        backButton.isVisible = isVisibleBackButton
        nextButton.isVisible = isVisibleNextButton
        backButtonText?.let {
            backButton.text = backButtonText
        }
    }

    @Suppress("UnusedPrivateMember")
    private fun collectAllDevicesWithProgressBarAndShowNextPage(project: Project) {
        ProgressManager.getInstance().run(object : Modal(
            project, UIBundle.message("ui.progress.webcam.title"), false
        ) {
            override fun run(indicator: ProgressIndicator) {
                if (GlobalPluginStorage.camerasInfo.isEmpty()) {
                    GlobalPluginStorage.camerasInfo.addAll(collectAllDevices())
                }
                loadBasePage(
                    WebcamChoicePageTemplate(GlobalPluginStorage.camerasInfo), "ui.button.select", true
                )
            }
        })
    }

    fun focusOnfFileById(task: Task, id: String?) {
        id?.let {
            TaskFileHandler.getVirtualFileByProjectTaskId(project, task, id)?.let {
                focusOnFile(it)
            }
        } ?: TaskFileHandler.projectToTaskToFiles[project]?.get(task)?.first()?.let {
            focusOnFile(it)
        } ?: logger.error("Can't find any file for '$task' task")
    }

    /**
     * Switches the editor to the virtualFile.
     */
    private fun focusOnFile(virtualFile: VirtualFile) {
        ApplicationManager.getApplication().invokeAndWait {
            FileEditorManager.getInstance(project).openFile(
                virtualFile, false
            )
        }
    }

    /**
     * Opening file in editor by html link.
     */
    fun listenFileRedirection(task: Task) {
        mainWindow.jslinkProcess(LinkType.FILE) {
            focusOnfFileById(task, it)
        }
    }

    /**
     * Redirecting to external websites in JCEF.
     */
    private fun listenRedirection() {
        mainWindow.jslinkProcess(LinkType.JCEF) {
            val previousBackState = backButton.getState()
            val previousNextState = nextButton.getState()
            nextButton.changeState(ButtonState(previousNextState.text, false))
            backButton.changeState(
                ButtonState(UIBundle.message("ui.button.back"), true) {
                    backButton.changeState(previousBackState)
                    nextButton.changeState(previousNextState)
                    mainWindow.loadCurrentTemplate()
                }
            )
        }
    }

    /**
     * @return **true** if any required field is not filled. **false** otherwise.
     */
    fun checkInputs(): Promise<Boolean> =
        mainWindow.executeJavaScriptAsync("allChecked()").then { it.toBoolean() }

    fun setNextAction(listener: ActionListener) = nextButton.addListener(listener)

    fun setBackAction(listener: ActionListener) = backButton.addListener(listener)
}
