package org.jetbrains.research.tasktracker.ui.main.panel.models

/**
 * Types of redirecting links.
 */
enum class LinkType(val queryFilter: String, val prevent: Boolean, val getAttribute: String) {
    DEFAULT_BROWSER(".defaultBrowser", true, "href"),
    JCEF("a:not(.defaultBrowser):not(.file)", false, "href"),
    FILE(".file", true, "data-value")
}
