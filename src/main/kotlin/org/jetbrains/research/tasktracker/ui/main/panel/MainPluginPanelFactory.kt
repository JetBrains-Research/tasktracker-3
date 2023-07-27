package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.jcef.JBCefApp
import com.intellij.util.ui.JBUI
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.models.Extension
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

class MainPluginPanelFactory : ToolWindowFactory {
    private val nextButton = createJButton("ui.button.next")
    private val backButton = createJButton("ui.button.back", isVisibleProp = false)

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

    private fun welcomePage() {
        loadBasePage(MainPageTemplate, "ui.button.next", false)
        nextButton.addListener {
            selectTask()
        }
    }

    // TODO refactor it for many configs
    private fun selectTask() {
        // TODO taskContentConfig can be null?
        loadBasePage(
            TasksPageTemplate(listOf(TaskTrackerPlugin.mainConfig.taskContentConfig ?: error("TODO"))),
            "ui.button.select",
            true
        )
        nextButton.addListener {
            mainWindow.getElementValue("language").onSuccess { result ->
                val task = TaskTrackerPlugin.mainConfig.taskContentConfig?.getTask(
                    Extension.values().find { it.name == result?.uppercase() }
                        ?: error("Cannot find extension $result")
                )
                    ?: error("taskContentConfig is null")
                ApplicationManager.getApplication().invokeAndWait {
                    TaskFileHandler.initTask(project, task)
                }
                focusOnFile(TaskFileHandler.projectToTaskToFiles[project]?.get(task)?.first() ?: error("Error"))
                solveTask(task)
            }.onError {
                error(it.localizedMessage)
            }
        }
        backButton.addListener {
            welcomePage()
        }
    }

    private fun focusOnFile(virtualFile: VirtualFile) {
        ApplicationManager.getApplication().invokeAndWait {
            FileEditorManager.getInstance(project)
                .openFile(
                    virtualFile,
                    false
                )
        }
    }

    private fun solveTask(task: Task) {
        // TODO to selected config
        loadBasePage(
            SolvePageTemplate(task),
            "ui.button.submit",
            true
        )
        backButton.addListener {
            selectTask()
        }
        nextButton.addListener {
            TaskFileHandler.disposeTask(project, task)
            welcomePage()
        }
    }

    private fun JButton.addListener(listener: ActionListener) {
        actionListeners.forEach {
            removeActionListener(it)
        }
        addActionListener(listener)
    }
}
