package org.jetbrains.research.tasktracker.tracking.activity.actions

/**
 * Enum containing actions responsible for cursor movement, which we do not want to designate as shortcuts.
 */
enum class MovingActions {

    /**
     * Moving the cursor left.
     */
    EditorLeft,

    /**
     * Moving the cursor right.
     */
    EditorRight,

    /**
     * Moving the cursor down.
     */
    EditorDown,

    /**
     * Moving the cursor up.
     */
    EditorUp,

    /**
     * Moving the cursor to line start.
     */
    EditorLineStart,

    /**
     * Moving the cursor to line end.
     */
    EditorLineEnd,

    /**
     * Moving the cursor up one line after the currently maximally visible one on the screen.
     */
    EditorPageUp,

    /**
     * Moving the cursor down one line after the currently maximally visible one on the screen.
     */
    EditorPageDown,

    /**
     * Moving the cursor to the previous word.
     */
    EditorPreviousWord,

    /**
     * Moving the cursor to the next word.
     */
    EditorNextWord,

    /**
     * Scrolls the surrounding scrollable area up by one unit.
     */
    EditorScrollUp,

    /**
     * Scrolls the surrounding scrollable area down by one unit.
     */
    EditorScrollDown,

    /**
     * Moving the cursor to the text start.
     */
    EditorTextStart,

    /**
     * Moving the cursor to the text end.
     */
    EditorTextEnd,

    /**
     * Moving the cursor down with selection.
     */
    EditorDownWithSelection,

    /**
     * Moving the cursor up with selection.
     */
    EditorUpWithSelection,

    /**
     * Moving the cursor right with selection.
     */
    EditorRightWithSelection,

    /**
     * Moving the cursor left with selection.
     */
    EditorLeftWithSelection,

    /**
     * Moving the cursor to line start with selection.
     */
    EditorLineStartWithSelection,

    /**
     * Moving the cursor to line end with selection.
     */
    EditorLineEndWithSelection,

    /**
     * Moving the cursor down one line after the currently maximally visible one on the screen with selection.
     */
    EditorPageDownWithSelection,

    /**
     * Moving the cursor up one line after the currently maximally visible one on the screen with selection.
     */
    EditorPageUpWithSelection,
}
