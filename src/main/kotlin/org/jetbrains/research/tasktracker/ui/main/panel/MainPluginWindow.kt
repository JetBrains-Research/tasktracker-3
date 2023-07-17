package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.JBCefBrowser
import org.jetbrains.research.tasktracker.ui.main.panel.template.ErrorPageTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.HtmlTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.IndexPageTemplate
import javax.swing.JComponent

class MainPluginWindow(service: MainWindowService) {
    private val windowBrowser: JBCefBrowser
    val jComponent: JComponent
        get() = windowBrowser.component

    init {
        windowBrowser = JBCefBrowser()
        windowBrowser.setErrorPage { errorCode, errorText, failedUrl ->
            ErrorPageTemplate.pageContent(errorCode.code.toString(), errorText, failedUrl)
        }
        loadDefaultPage(IndexPageTemplate)
        Disposer.register(service, windowBrowser)
    }

    fun loadDefaultPage(template: HtmlTemplate) = windowBrowser.loadHTML(template.pageContent())
}
