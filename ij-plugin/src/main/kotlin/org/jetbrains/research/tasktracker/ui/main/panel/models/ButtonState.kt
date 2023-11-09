package org.jetbrains.research.tasktracker.ui.main.panel.models

import java.awt.event.ActionListener

data class ButtonState(
    val text: String,
    val isVisibleProp: Boolean,
    val actionListener: ActionListener? = null
)
