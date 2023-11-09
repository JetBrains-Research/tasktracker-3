package org.jetbrains.research.tasktracker.tracking.activity.actions

/**
 * Enum containing actions responsible for typing actions, which we do not want to designate as shortcuts.
 */
enum class TypingActions {
    /**
     * Pressing the backspace key.
     */
    EditorBackSpace,

    /**
     * Pressing the enter key.
     */
    EditorEnter,

    /**
     * Pressing the tab key.
     */
    EditorTab,
}
