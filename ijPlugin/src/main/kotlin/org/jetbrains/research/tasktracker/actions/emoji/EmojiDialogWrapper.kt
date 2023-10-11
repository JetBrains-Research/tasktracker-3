package org.jetbrains.research.tasktracker.actions.emoji

import org.jetbrains.research.tasktracker.actions.BaseWrapper
import org.jetbrains.research.tasktracker.config.emoji.Emotion
import javax.swing.Icon

class EmojiDialogWrapper(emotion: Emotion) : BaseWrapper() {
    override val icon: Icon? = emotion.modalWindowIcon
    override val text: String = emotion.randomAdviceDescription

    init {
        isResizable = false
        title = "Self-Regulation Recommendation"
        cancelAction.isEnabled = false
        init()
    }
}
