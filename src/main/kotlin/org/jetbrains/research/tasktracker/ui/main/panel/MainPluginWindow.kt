package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.ide.ui.UISettingsListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.JBCefBrowser
import org.jetbrains.research.tasktracker.ui.main.panel.template.ErrorPageTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.HtmlTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.IndexPageTemplate
import javax.swing.JComponent

class MainPluginWindow(service: MainWindowService) {
    private val windowBrowser: JBCefBrowser
    private var currentTemplate: HtmlTemplate
    val jComponent: JComponent
        get() = windowBrowser.component

    init {
        currentTemplate = IndexPageTemplate
        windowBrowser = JBCefBrowser()
        windowBrowser.setErrorPage { errorCode, errorText, failedUrl ->
            ErrorPageTemplate.pageContent(errorCode.code.toString(), errorText, failedUrl)
        }
        loadDefaultPage(currentTemplate)
        val app = ApplicationManager.getApplication().messageBus
        app.connect().subscribe(
            UISettingsListener.TOPIC,
            UISettingsListener {
                loadDefaultPage(currentTemplate)
            }
        )
        Disposer.register(service, windowBrowser)
    }

    fun loadDefaultPage(template: HtmlTemplate) =
        windowBrowser.loadHTML(template.pageContent()).also { currentTemplate = template }
}
