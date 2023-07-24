package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.jcef.JBCefApp
import com.intellij.util.ui.JBUI
import org.jetbrains.research.tasktracker.ui.main.panel.template.HtmlTemplateBase
import org.jetbrains.research.tasktracker.ui.main.panel.template.MainPageTemplate
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
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
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

    private fun selectTask() {
        loadBasePage(TasksPageTemplate, "ui.button.select", true)
        backButton.addListener {
            loadBasePage(MainPageTemplate, "ui.button.next", false)
        }
    }

    private fun JButton.addListener(listener: ActionListener) {
        actionListeners.forEach {
            removeActionListener(it)
        }
        addActionListener(listener)
    }
}
