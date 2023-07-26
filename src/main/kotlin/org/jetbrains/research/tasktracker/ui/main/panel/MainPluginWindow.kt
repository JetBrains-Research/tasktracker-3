package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefClient
import com.intellij.ui.jcef.JsExpressionResult
import com.intellij.ui.jcef.executeJavaScriptAsync
import org.jetbrains.concurrency.Promise
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
        windowBrowser.jbCefClient.setProperty(JBCefClient.Properties.JS_QUERY_POOL_SIZE, JS_QUERY_POOL_SIZE)
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

    fun getElementValue(elementId: String): Promise<JsExpressionResult> =
        windowBrowser.executeJavaScriptAsync("document.getElementById('$elementId').value")

    fun loadHtmlTemplate(template: HtmlTemplateBase) = windowBrowser
        .loadHTML(template.pageContent(theme = currentTheme)).also { currentTemplate = template }

    companion object {
        const val JS_QUERY_POOL_SIZE = 100
    }
}
