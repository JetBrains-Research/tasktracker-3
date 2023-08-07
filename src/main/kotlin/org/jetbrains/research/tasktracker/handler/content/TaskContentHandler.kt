package org.jetbrains.research.tasktracker.handler.content

import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.content.TaskContentConfig
import org.jetbrains.research.tasktracker.config.content.TaskFileInfo
import org.jetbrains.research.tasktracker.config.content.TaskInfo
import org.jetbrains.research.tasktracker.handler.BaseHandler
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.DefaultContentProvider
import org.jetbrains.research.tasktracker.tracking.task.SourceSet
import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.tracking.task.TaskFile

class TaskContentHandler(override val config: TaskContentConfig) : BaseHandler {

    override fun setup() {
        TaskTrackerPlugin.taskIdTask = config.tasks.associate { it.id to it.getTask() }.toMutableMap()
    }

    private fun TaskInfo.getTask(): Task =
        object : Task {
            override val name: String
                get() = this@getTask.name
            override val description: String
                get() = this@getTask.description
            override val taskFiles: List<TaskFile>
                get() = this@getTask.files.map { file -> file.toTaskFile(this@getTask.language, this@getTask.name) }
            override val root: String
                get() = this@getTask.root
            override val focusFileId: String?
                get() = this@getTask.focusFileId
        }

    private fun TaskFileInfo.toTaskFile(language: Extension, taskName: String): TaskFile = object : TaskFile {
        private val path = "$taskName/$relativePath"
        override val content: String
            get() = templateFile?.getTemplate(path, extension) ?: DefaultContentProvider.getDefaultContent(
                this.extension,
                path
            )
        override val filename: String
            get() = this@toTaskFile.filename
        override val relativePath: String
            get() = this@toTaskFile.relativePath
        override val extension: Extension
            get() = this@toTaskFile.language ?: language
        override val sourceSet: SourceSet
            get() = this@toTaskFile.sourceSet
        override val id: String?
            get() = this@toTaskFile.id
    }

    private fun String.getTemplate(path: String, extension: Extension): String {
        val fileName = "$this${extension.ext}"
        val content = TaskInfo::class.java.getResource(fileName)?.readText()
            ?: error("There are no template file with name '$fileName'")
        val builder = StringBuilder(content)
        builder.insert(0, DefaultContentProvider.getPackage(extension, path))
        return builder.toString()
    }
}
