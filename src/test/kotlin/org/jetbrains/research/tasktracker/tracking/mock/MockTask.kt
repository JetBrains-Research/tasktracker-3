package org.jetbrains.research.tasktracker.tracking.mock

import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.DefaultContentProvider
import org.jetbrains.research.tasktracker.tracking.task.Task

data class MockTask(
    override val filename: String,
    override val extension: Extension,
    override val content: String? = null,
    override val relativeFilePath: String? = null
) : Task {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MockTask

        if (filename != other.filename) return false
        if (extension != other.extension) return false
        if (content != other.content) {
            if (content == null || other.content == null) {
                return content == DefaultContentProvider.getDefaultContent(this) ||
                    other.content == DefaultContentProvider.getDefaultContent(other)
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = filename.hashCode()
        result = 31 * result + extension.hashCode()
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (relativeFilePath?.hashCode() ?: 0)
        return result
    }
}
