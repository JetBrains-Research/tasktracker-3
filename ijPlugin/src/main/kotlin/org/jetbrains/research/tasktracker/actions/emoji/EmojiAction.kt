package org.jetbrains.research.tasktracker.actions.emoji

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ex.TooltipDescriptionProvider
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage

class EmojiAction : AnAction(), TooltipDescriptionProvider {
    override fun actionPerformed(e: AnActionEvent) {
        val emoji = GlobalPluginStorage.currentEmotion

        EmojiDialogWrapper(emoji).show()
    }

    override fun getActionUpdateThread() = ActionUpdateThread.EDT

    override fun update(e: AnActionEvent) {
        val emoji = GlobalPluginStorage.currentEmotion
        e.presentation.icon = emoji.icon
        // To be able to show the description in several lines
        e.presentation.text = ""
        e.presentation.description = emoji.getRandomAffirmDescription()
        super.update(e)
    }
}
