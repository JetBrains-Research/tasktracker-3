package org.jetbrains.research.tasktracker.config.content

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.DefaultContentProvider
import org.jetbrains.research.tasktracker.tracking.task.SourceSet
import org.jetbrains.research.tasktracker.tracking.task.TaskFile

@Serializable
class TaskFileDto(val filename: String, val sourceSet: SourceSet) {
    val extension: Extension? = null
    val templateFile: String? = null
    val relativePath: String = ""
    val id: String? = null

    fun toTaskFile(extension: Extension, taskName: String): TaskFile = object : TaskFile {
        private val path = "$taskName/$relativePath"
        override val content: String
            get() = templateFile?.getTemplate(path, extension) ?: DefaultContentProvider.getDefaultContent(
                this.extension,
                path
            )
        override val filename: String
            get() = this@TaskFileDto.filename
        override val relativePath: String
            get() = this@TaskFileDto.relativePath
        override val extension: Extension
            get() = this@TaskFileDto.extension ?: extension
        override val sourceSet: SourceSet
            get() = this@TaskFileDto.sourceSet
        override val id: String?
            get() = this@TaskFileDto.id
    }

    private fun String.getTemplate(path: String, extension: Extension): String {
        val fileName = "$this${extension.ext}"
        val content = TaskFileDto::class.java.getResource(fileName)?.readText()
            ?: error("There are no template file with name '$fileName'")
        val builder = StringBuilder(content)
        builder.insert(0, DefaultContentProvider.getPackage(extension, path))
        return builder.toString()
    }

    companion object {
        val defaultTaskFileDto = TaskFileDto("task", SourceSet.SRC)
    }
}
