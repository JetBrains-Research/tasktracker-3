package org.jetbrains.research.tasktracker.ui.main.panel

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.util.ui.UIUtil
import org.jetbrains.research.tasktracker.ui.main.panel.template.ErrorPageTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.HtmlTemplate
import org.jetbrains.research.tasktracker.ui.main.panel.template.IndexPageTemplate
import javax.swing.JComponent

class MainPluginWindow(service: MainWindowService) {
    private val windowBrowser: JBCefBrowser
    private var currentTemplate: HtmlTemplate
    private var isUnderDarcula: Boolean
    val jComponent: JComponent
        get() = windowBrowser.component

    init {
        isUnderDarcula = UIUtil.isUnderDarcula()
        currentTemplate = IndexPageTemplate
        windowBrowser = JBCefBrowser()
        windowBrowser.setErrorPage { errorCode, errorText, failedUrl ->
            ErrorPageTemplate.pageContent(errorCode.code.toString(), errorText, failedUrl)
        }
        loadDefaultPage(currentTemplate)
        val app = ApplicationManager.getApplication().messageBus
        app.connect().subscribe(
            LafManagerListener.TOPIC,
            LafManagerListener {
                if (isUnderDarcula != UIUtil.isUnderDarcula()) {
                    loadDefaultPage(currentTemplate)
                    isUnderDarcula = UIUtil.isUnderDarcula()
                }
            }
        )
        Disposer.register(service, windowBrowser)
    }

    fun loadDefaultPage(template: HtmlTemplate) =
        windowBrowser.loadHTML(template.pageContent()).also { currentTemplate = template }
}
