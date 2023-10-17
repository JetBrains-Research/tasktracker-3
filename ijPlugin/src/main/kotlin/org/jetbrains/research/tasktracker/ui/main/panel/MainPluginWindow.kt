package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.*
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandlerAdapter
import org.intellij.lang.annotations.Language
import org.jetbrains.concurrency.Promise
import org.jetbrains.research.tasktracker.ui.main.panel.models.Theme
import org.jetbrains.research.tasktracker.ui.main.panel.template.DefaultErrorPage
import org.jetbrains.research.tasktracker.ui.main.panel.template.HtmlTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.MainPageTemplate
import javax.swing.JComponent

class MainPluginWindow(service: MainWindowService) {
    private val windowBrowser: JBCefBrowser = JBCefBrowser()

    private var currentTheme = Theme.currentIdeTheme()
    private var currentTemplate: HtmlTemplate = MainPageTemplate.loadCurrentTemplate()
    private val handlers = mutableListOf<CefLoadHandlerAdapter>()
    val jComponent: JComponent
        get() = windowBrowser.component

    init {
        windowBrowser.jbCefClient.setProperty(JBCefClient.Properties.JS_QUERY_POOL_SIZE, JS_QUERY_POOL_SIZE)
        windowBrowser.setErrorPage { errorCode, errorText, failedUrl ->
            DefaultErrorPage(errorCode.code.toString(), errorText, failedUrl).htmlContent
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

    private fun getJsElementByIdCommand(elementId: String) = "document.getElementById('$elementId')"

    fun getElementValue(elementId: String): Promise<JsExpressionResult> =
        windowBrowser.executeJavaScriptAsync("${getJsElementByIdCommand(elementId)}.value")

    fun checkIfRadioButtonChecked(elementId: String): Promise<JsExpressionResult> =
        windowBrowser.executeJavaScriptAsync("${getJsElementByIdCommand(elementId)}.checked")

    fun executeJavaScriptAsync(code: String) = windowBrowser.executeJavaScriptAsync(code)

    fun executeJavascript(
        @Language("JavaScript") codeBeforeInject: String = "",
        @Language("JavaScript") codeAfterInject: String = "",
        queryResult: String = "",
        handler: (String) -> JBCefJSQuery.Response?
    ) {
        val jbCefJSQuery = JBCefJSQuery.create(windowBrowser as JBCefBrowserBase)
        val code =
            "${codeBeforeInject.trimIndent()} ${jbCefJSQuery.inject(queryResult)}; ${codeAfterInject.trimIndent()}"
        Disposer.register(windowBrowser, jbCefJSQuery)
        jbCefJSQuery.addHandler(handler)
        val newLoadHandler = object : CefLoadHandlerAdapter() {
            override fun onLoadEnd(browser: CefBrowser?, frame: CefFrame?, httpStatusCode: Int) {
                windowBrowser.cefBrowser.executeJavaScript(
                    code, windowBrowser.cefBrowser.url, 0
                )
                super.onLoadEnd(browser, frame, httpStatusCode)
            }
        }
        windowBrowser.jbCefClient.addLoadHandler(newLoadHandler, windowBrowser.cefBrowser)
        handlers.add(newLoadHandler)
    }

    fun removeHandlers() {
        handlers.forEach { handler ->
            windowBrowser.jbCefClient.removeLoadHandler(handler, windowBrowser.cefBrowser)
        }
        handlers.clear()
    }

    fun loadHtmlTemplate(template: HtmlTemplate) =
        windowBrowser.loadHTML(template.htmlContent).also { currentTemplate = template }

    companion object {
        const val JS_QUERY_POOL_SIZE = 100
    }
}
