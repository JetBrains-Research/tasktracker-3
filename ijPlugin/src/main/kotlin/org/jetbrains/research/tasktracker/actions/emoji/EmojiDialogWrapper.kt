package org.jetbrains.research.tasktracker.actions.emoji

import org.jetbrains.research.tasktracker.actions.BaseWrapper
import javax.swing.*

class EmojiDialogWrapper(emotion: EmotionType) : BaseWrapper() {
    override val icon: Icon? = emotion.modalWindowIcon
    override val text: String = emotion.getRandomAdviceDescription()

    init {
        isResizable = false
        title = "Self-Regulation Recommendation"
        cancelAction.isEnabled = false
        init()
    }
}
