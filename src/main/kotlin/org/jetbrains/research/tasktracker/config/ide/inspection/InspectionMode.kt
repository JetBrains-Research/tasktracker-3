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
     * Enable only inspections from the config.
     */
    ENABLE_SELECTED,

    /**
     * Keep user inspections with removing inspections from the config.
     */
    DISABLE_SELECTED,

    /**
     * Keep user inspections with adding inspections from the config.
     */
    ADD_SELECTED
}
