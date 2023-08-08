package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.jcef.JBCefApp
import com.intellij.util.ui.JBUI
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.tracking.TaskFileHandler
import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.ui.main.panel.template.HtmlTemplateBase
import org.jetbrains.research.tasktracker.ui.main.panel.template.MainPageTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.SolvePageTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.TasksPageTemplate
import org.jetbrains.research.tasktracker.util.UIBundle
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionListener
import javax.swing.JButton

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

    private lateinit var mainWindow: MainPluginWindow
    private lateinit var project: Project
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        this.project = project
        mainWindow = project.getService(MainWindowService::class.java).mainWindow
        mainWindow.jComponent.size = JBUI.size(toolWindow.component.width, toolWindow.component.height)
        nextButton.addListener {
            selectTask()
        }
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

    private fun loadBasePage(template: HtmlTemplateBase, buttonTextKey: String, isVisibleBackButton: Boolean) {
        mainWindow.loadHtmlTemplate(template)
        nextButton.text = UIBundle.message(buttonTextKey)
        backButton.isVisible = isVisibleBackButton
    }

    /**
     * Switches the panel to the plugin description window.
     */
    private fun welcomePage() {
        loadBasePage(MainPageTemplate, "ui.button.next", false)
        nextButton.addListener {
            selectTask()
        }
    }

    // TODO refactor it for many configs
    /**
     * Switches the panel to the task selection window.
     */
    private fun selectTask() {
        loadBasePage(
            TasksPageTemplate(TaskTrackerPlugin.taskIdTask.values.toList()),
            "ui.button.select",
            true
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
        val task = TaskTrackerPlugin.taskIdTask.values.find { it.name == name }
            ?: error("Can't find task with name '$name'")
        ApplicationManager.getApplication().invokeAndWait {
            TaskFileHandler.initTask(project, task)
        }
        task.focusFileId?.let { id ->
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
            FileEditorManager.getInstance(project)
                .openFile(
                    virtualFile,
                    false
                )
        }
    }

    /**
     * Switches the panel to the task solving window.
     * It contains task name, description and I/O data.
     */
    private fun solveTask(task: Task) {
        loadBasePage(
            SolvePageTemplate(task),
            "ui.button.submit",
            true
        )
        backButton.addListener {
            TaskFileHandler.disposeTask(project, task)
            mainWindow.removeHandlers()
            selectTask()
        }
        nextButton.addListener {
            TaskFileHandler.disposeTask(project, task)
            mainWindow.removeHandlers()
            welcomePage()
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
}
