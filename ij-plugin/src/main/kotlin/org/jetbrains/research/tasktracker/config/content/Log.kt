package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable

/**
 * Represents a log with associated log paths and a log type.
 *
 * @param logPaths The list of relative log paths from the project root.
 * @param type The log type. By default, it is set to "default".
 */
@Serializable
data class Log(val logPaths: List<String>, val isInPluginDirectory: Boolean = false, val type: String = "default")
