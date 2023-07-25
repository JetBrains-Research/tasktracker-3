package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.JBCefBrowser
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme
import org.jetbrains.research.tasktracker.ui.main.panel.template.ErrorPageTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.HtmlTemplateBase
import org.jetbrains.research.tasktracker.ui.main.panel.template.MainPageTemplate
import javax.swing.JComponent

class MainPluginWindow(service: MainWindowService) {
    private val windowBrowser: JBCefBrowser = JBCefBrowser()

    private var currentTheme = Theme.currentIdeTheme()
    private var currentTemplate: HtmlTemplateBase = MainPageTemplate
    val jComponent: JComponent
        get() = windowBrowser.component

    init {
        windowBrowser.setErrorPage { errorCode, errorText, failedUrl ->
            ErrorPageTemplate.pageContent(theme = currentTheme, errorCode.code.toString(), errorText, failedUrl)
        }
        loadHtmlTemplate(currentTemplate)
        val app = ApplicationManager.getApplication().messageBus
        app.connect().subscribe(
            LafManagerListener.TOPIC,
            LafManagerListener {
                if (currentTheme != Theme.currentIdeTheme()) {
                    currentTheme = Theme.currentIdeTheme()
                    loadHtmlTemplate(currentTemplate)
                }
            }
        )
        Disposer.register(service, windowBrowser)
    }

    fun loadHtmlTemplate(template: HtmlTemplateBase) = windowBrowser
        .loadHTML(template.pageContent(theme = currentTheme)).also { currentTemplate = template }
}
