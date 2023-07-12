package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.jcef.JBCefApp

class MainPluginPanelFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val mainWindow = project.getService(MainWindowService::class.java).mainWindow
        val jComponent = toolWindow.component
        jComponent.parent.add(mainWindow.jComponent)
    }

    override fun isApplicable(project: Project) = super.isApplicable(project) && JBCefApp.isSupported()
}
