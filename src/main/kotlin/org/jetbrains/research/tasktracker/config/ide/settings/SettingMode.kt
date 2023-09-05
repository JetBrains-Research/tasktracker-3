package org.jetbrains.research.tasktracker.config.ide.settings

enum class SettingMode {
    /**
     * This means that user settings remain unchanged,
     * and they are not relevant to our research.
     */
    DEFAULT,

    /**
     * Switch setting on.
     */
    ON,

    /**
     * Switch setting off.
     */
    OFF
}
