package org.jetbrains.research.tasktracker.config.content.task.base

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.content.task.ProgrammingTask
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.DefaultContentProvider
import org.jetbrains.research.tasktracker.tracking.task.SourceSet

interface ITaskFileInfo {
    val filename: String
    val sourceSet: SourceSet

    /**
     * Task file extension
     */
    val extension: Extension?

    /**
     * Template name that exists in the `configRoot/content`
     * and will be subsequently loaded into the project.
     */
    val templateFile: String?

    /**
     * task file relative path.
     */
    val relativePath: String

    /**
     * Gets the content of the file as a string.
     * @return The content of the file as a string, or null if the file is empty.
     */
    val content: String?

    /**
     * id is intended for quick access to the corresponding VirtualFile.
     */
    val id: String?
}

@Serializable
data class TaskFileInfo(override val filename: String, override val sourceSet: SourceSet) : ITaskFileInfo {
    override var extension: Extension? = null
    override val templateFile: String? = null
    override val relativePath: String = ""

    // TODO: can we avoid using var here?
    override var content: String? = gatherContent()
    override val id: String? = null

    fun gatherContent() = extension?.let {
        templateFile?.getTemplate(relativePath, it) ?: DefaultContentProvider.getDefaultContent(
            extension,
            relativePath
        )
    }

    companion object {
        private fun String.getTemplate(path: String, extension: Extension): String {
            val fileName = "$this${extension.ext}"
            val content = ProgrammingTask::class.java.getResource(fileName)?.readText()
                ?: error("There are no template file with name '$fileName'")
            val builder = StringBuilder(content)
            builder.insert(0, DefaultContentProvider.getPackage(extension, path))
            return builder.toString()
        }
    }
}
