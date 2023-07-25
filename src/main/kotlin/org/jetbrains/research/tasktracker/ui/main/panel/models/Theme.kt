package org.jetbrains.research.tasktracker.ui.main.panel.models

import com.intellij.util.ui.UIUtil

enum class Theme(val cssFileName: String) {
    DARK("dark"),
    LIGHT("light")
    ;

    companion object {
        fun currentIdeTheme() = if (UIUtil.isUnderDarcula()) {
            DARK
        } else {
            LIGHT
        }
    }
}
