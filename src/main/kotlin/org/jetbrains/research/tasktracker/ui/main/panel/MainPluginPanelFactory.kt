package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.jcef.JBCefApp
import org.jetbrains.research.tasktracker.ui.main.panel.template.TasksPageTemplate
import java.awt.GridLayout
import java.awt.event.ActionListener
import javax.swing.JButton

class MainPluginPanelFactory : ToolWindowFactory {
    private val button = JButton("click me")

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val mainWindow = project.getService(MainWindowService::class.java).mainWindow
        val jComponent = toolWindow.component
        val panel = JBPanel<JBPanel<*>>(GridLayout(2, 1)).apply { // TODO need we GridLayout?
            add(mainWindow.jComponent)
            add(
                JBPanel<JBPanel<*>>().apply {
                    add(
                        button.apply {
                            addListener {
                                mainWindow.loadDefaultPage(TasksPageTemplate)
                                // TODO refactor, add function for button listener and text change
                            }
                        }
                    )
                }
            )
        }
        jComponent.parent.add(panel)
    }

    override fun isApplicable(project: Project) = super.isApplicable(project) && JBCefApp.isSupported()

    private fun JButton.addListener(listener: ActionListener) {
        actionListeners.forEach {
            removeActionListener(it)
        }
        addActionListener(listener)
    }
}
