package org.jetbrains.research.tasktracker.tracking.activity

import org.joda.time.DateTime

data class ActivityEvent(
    val time: DateTime,
    val type: Type,
    val info: String,
    val selectedText: String = ""
)

enum class Type {
    Action,
    Execution,
    Shortcut,
    KeyPressed,
    KeyReleased
}
