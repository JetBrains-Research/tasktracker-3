package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.JBCefBrowser
import javax.swing.JComponent

class MainPluginWindow(service: MainWindowService) {
    private val windowBrowser: JBCefBrowser
    val jComponent: JComponent
        get() = windowBrowser.component
    init {
        windowBrowser = JBCefBrowser()
        // TODO: add main page view
        Disposer.register(service, windowBrowser)
    }
}
