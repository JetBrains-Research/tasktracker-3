package org.jetbrains.research.tasktracker.ui.main.panel

import org.jetbrains.research.tasktracker.util.UIBundle
import javax.swing.JButton

fun createJButton(
    uiBundleMessageKey: String,
    isBorderPaintedProp: Boolean = false,
    isVisibleProp: Boolean = true,
) = JButton(UIBundle.message(uiBundleMessageKey)).apply {
    isBorderPainted = isBorderPaintedProp
    isVisible = isVisibleProp
}
