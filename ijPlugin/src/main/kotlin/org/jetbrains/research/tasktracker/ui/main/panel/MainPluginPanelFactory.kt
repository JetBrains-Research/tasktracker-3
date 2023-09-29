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
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.config.content.task.base.Task
import org.jetbrains.research.tasktracker.config.content.task.base.TaskWithFiles
import org.jetbrains.research.tasktracker.modelInference.model.EmoModel
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.TaskFileHandler
import org.jetbrains.research.tasktracker.tracking.activity.ActivityTracker
import org.jetbrains.research.tasktracker.tracking.fileEditor.FileEditorTracker
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowTracker
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamTracker
import org.jetbrains.research.tasktracker.tracking.webcam.collectAllDevices
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage
import org.jetbrains.research.tasktracker.ui.main.panel.template.*
import org.jetbrains.research.tasktracker.util.UIBundle
import org.jetbrains.research.tasktracker.util.survey.SurveyParser
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionListener
import java.io.File
import javax.swing.JButton

/**
 * The class is intended to manage JCEF and Swing components simultaneously,
 * and its methods primarily serve as a way to switch between frames.
 *
 * Note: This class requires JBCefApp to be supported for proper functioning.
 *
 */
@Suppress("TooManyFunctions")
class MainPluginPanelFactory : ToolWindowFactory {
    // TODO: init in other place, states can be saved between sessions
    private val nextButton = createJButton("ui.button.next")
    private val backButton = createJButton("ui.button.back", isVisibleProp = false)
    private val logger: Logger = Logger.getInstance(MainPluginPanelFactory::class.java)

    private val trackers: MutableList<BaseTracker> = mutableListOf()

    private lateinit var mainWindow: MainPluginWindow
    private lateinit var project: Project

    private val client = HttpClient(CIO)
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        MainPanelStorage.userId = getUserId()
        this.project = project
        mainWindow = project.getService(MainWindowService::class.java).mainWindow
        mainWindow.jComponent.size = JBUI.size(toolWindow.component.width, toolWindow.component.height)
        welcomePage()
        val buttonPanel = JBPanel<JBPanel<*>>(FlowLayout()).apply {
            add(backButton)
            add(nextButton)
        }
        val panel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            add(mainWindow.jComponent)
            add(buttonPanel, BorderLayout.SOUTH)
        }
        toolWindow.component.add(panel)
    }

    override fun isApplicable(project: Project) = super.isApplicable(project) && JBCefApp.isSupported()

    private fun webWelcomePage() {
        nextButton.text = UIBundle.message("ui.button.next")
        backButton.isVisible = false
        mainWindow.loadHtmlTemplate(MainPageTemplate.loadCurrentTemplate())
        nextButton.addListener {
            webCamPage()
        }
    }

    private fun webSolvePage() {
        nextButton.text = UIBundle.message("ui.button.next")
        backButton.isVisible = true
//        mainWindow.loadHtmlTemplate(SolveWebPageTemplate.loadCurrentTemplate()) // TODO replace to the solvePage

        startTracking()

        backButton.addListener {
            webCamPage()
        }
        nextButton.addListener {
            survey()
        }
    }

    private fun startTracking() {
        if (trackers.isNotEmpty()) { // Otherwise we can lose data
            return
        }
        trackers.clear()

        // TODO: make better shared loggers
        GlobalPluginStorage.emoPredictor = EmoModel()
        val webCamTracker = WebCamTracker(project, GlobalPluginStorage.emoPredictor!!)
        trackers.addAll(
            listOf(
                ActivityTracker(project),
                ToolWindowTracker(project),
                webCamTracker,
                FileEditorTracker(project)
            )
        )
        trackers.forEach { it.startTracking() }
    }

    private fun loadBasePage(template: HtmlTemplate, buttonTextKey: String, isVisibleBackButton: Boolean) {
        mainWindow.loadHtmlTemplate(template)
        nextButton.text = UIBundle.message(buttonTextKey)
        backButton.isVisible = isVisibleBackButton
    }

    /**
     * Switches the panel to the plugin description window.
     */
    private fun welcomePage() {
        loadBasePage(MainPageTemplate.loadCurrentTemplate(), "ui.button.next", false)
        nextButton.addListener {
            selectTask()
        }
    }

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

    /**
     * Switches the panel to select a webcam.
     */
    private fun webCamPage() {
        MainPanelStorage.currentResearchId = getResearchId()
        with(MainPanelStorage) {
            registerResearch(userId, currentResearchId)
        }
        collectAllDevicesWithProgressBarAndShowNextPage(project)
        nextButton.text = UIBundle.message("ui.button.select")
        backButton.isVisible = true
        backButton.addListener {
            webWelcomePage()
        }
        nextButton.addListener {
            mainWindow.getElementValue("cameras").onSuccess { deviceNumber ->
                GlobalPluginStorage.currentDeviceNumber = deviceNumber?.toInt()
                webSolvePage()
            }.onError {
                error(it.localizedMessage)
            }
        }
    }

    // TODO refactor it for many configs
    /**
     * Switches the panel to the task selection window.
     */
    private fun selectTask() {
        loadBasePage(
            TasksPageTemplate(MainPanelStorage.taskIdTask.values.toList()), "ui.button.select", true
        )
        nextButton.addListener {
            mainWindow.getElementValue("tasks").onSuccess { name ->
                processTask(name.toString())
            }.onError {
                error(it.localizedMessage)
            }
        }
        backButton.addListener {
            welcomePage()
        }
    }

    /**
     * Loads configs by selected task and language
     */
    private fun processTask(name: String) {
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

    private fun focusOnfFileById(task: Task, id: String?) {
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

    private fun survey() {
        loadBasePage(
            SurveyTemplate.loadCurrentTemplate(), "ui.button.submit", true
        )
        nextButton.addListener {
            val surveyParser = SurveyParser(mainWindow, project)
            // TODO: rewrite
            GlobalScope.launch {
                surveyParser.parseAndLog()
                // TODO: unify
                val isSuccessful = sendSurvey(surveyParser.surveyLogger.getLogFiles())
                if (!isSuccessful) {
                    serverErrorPage()
                }
                trackers.forEach {
                    it.stopTracking()
                }
                trackers.forEach {
                    val isSuccessful = when (it) {
                        is ActivityTracker -> sendActivityFiles(it)
                        is WebCamTracker -> sendWebcamFiles(it)
                        is ToolWindowTracker -> sendToolWindowFiles(it)
                        is FileEditorTracker -> sendFileEditorFiles(it)
                        else -> false
                    }
                    if (!isSuccessful) {
                        serverErrorPage()
                    }
                }
                trackers.clear()
                resetAllIds()
                webFinalPage()
            }
        }
        backButton.addListener {
        }
    }

    private fun resetAllIds() {
        MainPanelStorage.currentResearchId = null
        MainPanelStorage.userId = null
    }

    private fun serverErrorPage() {
        resetAllIds()
        trackers.clear()
        loadBasePage(
            ServerErrorPage(), "ui.button.welcome", false
        )
        nextButton.isVisible = false
    }

    private fun webFinalPage() {
        loadBasePage(
            FinalPageTemplate.loadCurrentTemplate(), "ui.button.welcome", false
        )
        nextButton.addListener {
            welcomePage()
        }
    }

    /**
     * Switches the panel to the task solving window.
     * It contains task name, description and I/O data.
     */
    private fun solveTask(task: Task) {
        val activityTracker = ActivityTracker(project)
        activityTracker.startTracking()
        loadBasePage(
            SolvePageTemplate(task), "ui.button.submit", true
        )
        backButton.addListener {
            TaskFileHandler.disposeTask(project, task)
            mainWindow.removeHandlers()
            selectTask()
            activityTracker.stopTracking()
        }
        nextButton.addListener {
            TaskFileHandler.disposeTask(project, task)
            mainWindow.removeHandlers()
            welcomePage()
            activityTracker.stopTracking()
        }
        listenFileRedirection(task)
    }

    private fun listenFileRedirection(task: Task) {
        mainWindow.executeJavascript(
            """
            const files = document.getElementsByClassName('file');  
            for (const file of files) {
            file.addEventListener('click', function(event) {
                event.preventDefault();
            })
                file.onclick = load_file
            }
                function load_file (){
                 file_id = this.getAttribute('data-value');
                
            """,
            "}",
            "file_id"
        ) {
            focusOnfFileById(task, it)
            null
        }
    }

    private fun JButton.addListener(listener: ActionListener) {
        actionListeners.forEach {
            removeActionListener(it)
        }
        addActionListener(listener)
    }

    private fun getUserId() = getId("get-user-id")
    private fun getResearchId() = getId("get-research-id")

    @Suppress("TooGenericExceptionCaught")
    private fun getId(request: String): Int? = runBlocking {
        try {
            client.get("$DOMAIN/$request").body()
        } catch (e: Exception) {
            logger.warn("Server interaction error! Request: $request", e)
            null
        }
    }

    private fun sendToolWindowFiles(toolWindowTracker: ToolWindowTracker) = toolWindowTracker.getLogFiles().all {
        sendFile(it, "toolWindow")
    }

    private fun sendActivityFiles(activityTracker: ActivityTracker) = activityTracker.getLogFiles().all {
        sendFile(it, "activity")
    }

    private fun sendFileEditorFiles(fileEditorTracker: FileEditorTracker) = fileEditorTracker.getLogFiles().all {
        sendFile(it, "fileEditor")
    }

    private fun sendWebcamFiles(webCamTracker: WebCamTracker) = webCamTracker.getLogFiles().all {
        sendFile(it, "webCam")
    }

    private fun sendSurvey(surveyFiles: List<File>) = surveyFiles.all { sendFile(it, "survey") }

    @Suppress("TooGenericExceptionCaught")
    private fun sendFile(file: File, subdir: String) = runBlocking {
        try {
            client.submitFormWithBinaryData(
                url = "$DOMAIN/upload-document/${MainPanelStorage.currentResearchId}?subdir=$subdir",
                formData = formData {
                    append(
                        "file",
                        file.readBytes(),
                        Headers.build {
                            append(
                                HttpHeaders.ContentDisposition, "filename=\"${file.name}\""
                            )
                        }
                    )
                }
            )
            true
        } catch (e: Exception) {
            logger.warn("Server interaction error! File to send: ${file.path}", e)
            false
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun registerResearch(userId: Int?, researchId: Int?, name: String = "hackathon2023") { // TODO name
        if (userId == null || researchId == null) {
            logger.warn(
                "Found problems with the server connection! " +
                        "Continue doing this research offline. userId=$userId, researchId=$researchId"
            )
            return
        }
        runBlocking {
            val url = "$DOMAIN/create-research"
            try {
                client.submitForm(
                    url = url,
                    formParameters = parameters {
                        append("id", researchId.toString())
                        append("user_id", userId.toString())
                        append("name", name)
                    }
                )
            } catch (e: Exception) {
                logger.warn("Server interaction error! Url: $url", e)
            }
        }
    }

    companion object {
        const val DOMAIN = "http://3.249.245.244:8888"
    }
}
