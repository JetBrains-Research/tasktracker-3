package org.jetbrains.research.tasktracker.config.content.task

import kotlinx.serialization.Serializable

/***
 * This class stores an input and output example for a task
 */
@Serializable
data class InputOutput(val input: String, val output: String)
