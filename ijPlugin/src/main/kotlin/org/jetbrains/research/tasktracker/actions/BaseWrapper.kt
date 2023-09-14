package org.jetbrains.research.tasktracker.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBFont
import com.intellij.util.ui.JBUI
import javax.swing.*

abstract class BaseWrapper(open val project: Project? = null, canBeParent: Boolean = false) :
    DialogWrapper(project, canBeParent) {
    abstract val icon: Icon?
    abstract val text: String

    @Suppress("MagicNumber")
    override fun createCenterPanel(): JComponent {
        val appIcon = icon
        val box = getText()
        val icon = JLabel(appIcon)
        icon.setVerticalAlignment(SwingConstants.TOP)
        icon.setBorder(JBUI.Borders.empty(20, 12, 0, 12))
        box.setBorder(JBUI.Borders.empty(20, 0, 0, 20))
        return JBUI.Panels.simplePanel().addToLeft(icon).addToCenter(box)
    }

    private fun getText() = Box.createVerticalBox()
        .also { it.add(label(text, JBFont.medium())) }

    private fun label(text: String, font: JBFont) = JBLabel(text).withFont(font).also { it.setCopyable(true) }
}
