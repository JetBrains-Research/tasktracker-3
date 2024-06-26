package org.jetbrains.research.tasktracker.handler.ide

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.ide.actions.ToggleZenModeAction
import com.intellij.ide.ui.LafManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.ide.settings.SettingMode
import org.jetbrains.research.tasktracker.config.ide.settings.SettingsConfig
import org.jetbrains.research.tasktracker.config.ide.settings.Theme
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler
import javax.swing.UIManager.LookAndFeelInfo

class SettingsHandler(override val config: SettingsConfig, override val project: Project) : BaseProjectHandler {
    private val settings: CodeInsightSettings = CodeInsightSettings.getInstance()
    private val defaultUserCompletion: Boolean = settings.AUTO_POPUP_COMPLETION_LOOKUP
    private val lafManager = LafManager.getInstance()
    private val defaultUserLookAndFeel: LookAndFeelInfo? = LafManager.getInstance().currentLookAndFeel

    override fun setup() {
        setupCodeCompletion()
        setupZenMode(project)
        setupTheme()
    }

    private fun setupCodeCompletion() = when (config.enableCodeCompletion) {
        SettingMode.DEFAULT -> {}
        SettingMode.ON -> settings.AUTO_POPUP_COMPLETION_LOOKUP = true
        SettingMode.OFF -> settings.AUTO_POPUP_COMPLETION_LOOKUP = false
    }

    private fun setupZenMode(project: Project) {
        val isZenModeEnabled = ToggleZenModeAction.isZenModeEnabled(project)
        when (config.enableZenMode) {
            SettingMode.DEFAULT -> {}
            SettingMode.ON -> if (!isZenModeEnabled) changeZenModeState()
            SettingMode.OFF -> if (isZenModeEnabled) changeZenModeState()
        }
    }

    /**
     * Turns on Zen mode if it's off and turns it off if it's on.
     */
    private fun changeZenModeState() {
        val actionManager = ActionManager.getInstance()
        ApplicationManager.getApplication().invokeLater {
            actionManager.getAction(zenModeActionId)?.let { action ->
                actionManager.tryToExecute(action, null, null, null, true)
            }
        }
    }

    private fun setupTheme() {
        when (config.theme) {
            Theme.DEFAULT -> {}
            Theme.LIGHT -> changeTheme(light)
            Theme.DARCULA -> changeTheme(darcula)
        }
        lafManager.updateUI()
    }

    private fun changeTheme(themeName: String) {
        val theme = lafManager.installedLookAndFeels.find { it.name == themeName }
            ?: error("$themeName theme must exist")
        lafManager.currentLookAndFeel = theme
    }

    override fun destroy() {
        settings.AUTO_POPUP_COMPLETION_LOOKUP = defaultUserCompletion
        lafManager.currentLookAndFeel = defaultUserLookAndFeel
        lafManager.updateUI()
    }

    companion object {
        const val zenModeActionId = "ToggleZenMode"
        const val darcula = "Darcula"
        const val light = "Light"
    }
}
