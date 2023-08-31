package org.jetbrains.research.tasktracker.config.ide.inspection

enum class InspectionMode {
    /**
     * Enable all available inspections.
     */
    ALL,

    /**
     * Disable all available inspections.
     */
    NONE,

    /**
     * Keep user inspection settings.
     */
    DEFAULT,

    /**
     * Enable only selected inspections.
     */
    ENABLE_SELECTED,

    /**
     * Keep user inspections without selected inspections.
     */
    DISABLE_SELECTED,

    /**
     * Keep user inspections with adding selected inspections.
     */
    ADD_SELECTED
}
