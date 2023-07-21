package org.jetbrains.research.tasktracker.tracking.task

import org.jetbrains.research.tasktracker.models.Extension

/**
 * Represents a file related to a specific task and will be tracked further.
 */
interface TaskFile {

    /**
     * Gets the content of the file as a string.
     * @return The content of the file as a string, or null if the file is empty.
     */
    val content: String?

    /**
     * Gets the filename of the file.
     * @return The filename of the file.
     */
    val filename: String

    /**
     * Gets the relative path of the file from the root directory of the task.
     * @return The relative path of the file.
     */
    val relativePath: String

    /**
     * Gets the extension of the file.
     * @return The extension of the file.
     */
    val extension: Extension

    /**
     * Gets the source set to which this file belongs.
     * The source set is a logical grouping of source files in a build system.
     * @return The source set associated with this file.
     */
    val sourceSet: SourceSet
}
