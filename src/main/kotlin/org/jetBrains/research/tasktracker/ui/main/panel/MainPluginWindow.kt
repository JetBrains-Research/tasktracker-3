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
        windowBrowser.setErrorPage { _, _, _ -> // TODO insert parameters to error page
            defaultPageContent("error")
        }
        loadDefaultPage("index")
        Disposer.register(service, windowBrowser)
    }

    fun loadDefaultPage(name: String) = windowBrowser.loadHTML(defaultPageContent(name))

    private fun defaultPageContent(name: String) =
        MainPluginWindow::class.java.getResource("template/$name.html")?.readText()
            ?: error("Cannot find default page with name $name")
}
