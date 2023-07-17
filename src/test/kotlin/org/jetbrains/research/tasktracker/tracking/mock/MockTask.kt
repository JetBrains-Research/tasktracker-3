package org.jetbrains.research.tasktracker.tracking.mock

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
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
                return content == DefaultContentProvider.getDefaultContent(other) ||
                    other.content == DefaultContentProvider.getDefaultContent(this)
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

fun VirtualFile.toMockTask(project: Project): MockTask {
    return MockTask(
        nameWithoutExtension,
        Extension.values().find { it.ext == ".$extension" } ?: error("Unexpected extension"),
        FileDocumentManager.getInstance().getDocument(this)?.text,
        path.removePrefix("${project.basePath}/${MainTaskTrackerConfig.PLUGIN_NAME}/").removeSuffix("/$name")
    )
}
