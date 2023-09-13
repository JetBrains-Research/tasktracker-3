package org.jetbrains.research.tasktracker.actions.emoji

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBFont
import com.intellij.util.ui.JBUI
import javax.swing.Box
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.SwingConstants

class EmojiDialogWrapper(private val emotion: EmotionType) : DialogWrapper(null, false) {
    init {
        isResizable = false
        title = "Self-Regulation Recommendation"
        cancelAction.isEnabled = false
        init()
    }

    @Suppress("MagicNumber")
    override fun createCenterPanel(): JComponent {
        val appIcon = emotion.modalWindowIcon
        val box = getText()
        val icon = JLabel(appIcon)
        icon.setVerticalAlignment(SwingConstants.TOP)
        icon.setBorder(JBUI.Borders.empty(20, 12, 0, 12))
        box.setBorder(JBUI.Borders.empty(20, 0, 0, 20))
        return JBUI.Panels.simplePanel().addToLeft(icon).addToCenter(box)
    }

    override fun doOKAction() {
        // TODO: make it better
        EmojiAction.canBeChanged = true
        super.doOKAction()
    }

    private fun getText() = Box.createVerticalBox()
        .also { it.add(label(emotion.getRandomAdviceDescription(), JBFont.medium())) }

    private fun label(text: String, font: JBFont) = JBLabel(text).withFont(font).also { it.setCopyable(true) }
}
