package org.jetbrains.research.tasktracker.handler.ide

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.ide.settings.SettingMode
import org.jetbrains.research.tasktracker.config.ide.settings.SettingsConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler

class SettingsHandler(override val config: SettingsConfig) : BaseHandler {
    private val settings: CodeInsightSettings = CodeInsightSettings.getInstance()
    private val defaultUserCompletion: Boolean = settings.AUTO_POPUP_COMPLETION_LOOKUP

    override fun setup(project: Project) {
        setupCodeCompletion()
    }

    private fun setupCodeCompletion() = when (config.enableCodeCompletion) {
        SettingMode.DEFAULT -> {}
        SettingMode.ON -> settings.AUTO_POPUP_COMPLETION_LOOKUP = true
        SettingMode.OFF -> settings.AUTO_POPUP_COMPLETION_LOOKUP = false
    }

    override fun destroy() {
        settings.AUTO_POPUP_COMPLETION_LOOKUP = defaultUserCompletion
    }
}
