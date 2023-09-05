package org.jetbrains.research.tasktracker.handler.ide

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.ide.completion.CompletionConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler

class CompletionHandler(override val config: CompletionConfig) : BaseHandler {
    private val settings: CodeInsightSettings = CodeInsightSettings.getInstance()
    private val defaultCompletion: Boolean = settings.AUTO_POPUP_COMPLETION_LOOKUP

    override fun setup(project: Project) {
        settings.AUTO_POPUP_COMPLETION_LOOKUP = config.enableCompletion
    }

    override fun destroy() {
        settings.AUTO_POPUP_COMPLETION_LOOKUP = defaultCompletion
    }
}
