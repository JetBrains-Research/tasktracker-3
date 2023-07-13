package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class MainWindowService : Disposable {
    val mainWindow = MainPluginWindow(this)
    override fun dispose() {
        logger<Project>().info("Jcef window service was disposed")
    }
}
