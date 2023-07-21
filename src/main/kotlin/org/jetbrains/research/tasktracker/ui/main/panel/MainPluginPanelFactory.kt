package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.jcef.JBCefApp
import com.intellij.util.ui.JBUI
import org.jetbrains.research.tasktracker.ui.main.panel.template.IndexPageTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.TasksPageTemplate
import org.jetbrains.research.tasktracker.util.UIBundle
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.ActionListener
import javax.swing.JButton

class MainPluginPanelFactory : ToolWindowFactory {
    private val nextButton = buttonNonPaintedBorder(UIBundle.message("next"))
    private val backButton = buttonNonPaintedBorder(UIBundle.message("back"))
    private lateinit var mainWindow: MainPluginWindow

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        mainWindow = project.getService(MainWindowService::class.java).mainWindow
        val jComponent = toolWindow.component
        backButton.isVisible = false
        nextButton.addListener {
            selectTask()
        }
        mainWindow.jComponent.size = JBUI.size(toolWindow.component.width, toolWindow.component.height)
        val buttonPanel = JBPanel<JBPanel<*>>(FlowLayout()).apply {
            add(backButton)
            add(nextButton)
        }
        val panel = JBPanel<JBPanel<*>>(BorderLayout()).apply {
            add(mainWindow.jComponent)
            add(buttonPanel, BorderLayout.SOUTH)
        }
        jComponent.add(panel)
    }

    override fun isApplicable(project: Project) = super.isApplicable(project) && JBCefApp.isSupported()

    private fun selectTask() {
        mainWindow.loadDefaultPage(TasksPageTemplate)
        nextButton.text = UIBundle.message("select")
        backButton.isVisible = true
        backButton.addListener {
            mainWindow.loadDefaultPage(IndexPageTemplate)
            nextButton.text = UIBundle.message("next")
            backButton.isVisible = false
        }
    }

    private fun JButton.addListener(listener: ActionListener) {
        actionListeners.forEach {
            removeActionListener(it)
        }
        addActionListener(listener)
    }

    private fun buttonNonPaintedBorder(text: String) = JButton(text).apply {
        isBorderPainted = false
    }
}
