package org.jetbrains.research.tasktracker.tracking.mock

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.DefaultContentProvider
import org.jetbrains.research.tasktracker.tracking.task.SourceSet
import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.tracking.task.TaskFile

data class MockTask(
    override val name: String,
    override val taskFiles: List<TaskFile>,
    override val root: String = "",
    override val description: String = ""
) : Task

data class MockTaskFile(
    override val filename: String,
    override val extension: Extension,
    override val relativePath: String = "",
    override val content: String = DefaultContentProvider.getDefaultContent(extension, relativePath),
) : TaskFile {
    override val sourceSet: SourceSet = SourceSet.SRC
}

fun List<VirtualFile>.toMockTask(project: Project): MockTask {
    val firstFile = this.firstOrNull() ?: error("Empty list is unexpected")
    val taskFiles = map { file ->
        println(file.path)
        MockTaskFile(
            file.nameWithoutExtension,
            file.extensionEnum(),
            file.getRelativePath(project),
            FileDocumentManager.getInstance().getDocument(file)?.text ?: error("file `${file.name}` does not exist"),
        )
    }
    return MockTask(firstFile.nameWithoutExtension, taskFiles, firstFile.getRoot(project))
}

private fun VirtualFile.getRoot(project: Project) =
    getRootAndRelativePath(project).first().takeIf { it != "src" }.orEmpty()

private fun VirtualFile.getRelativePath(project: Project) =
    getRootAndRelativePath(project).last().takeIf { it != nameWithoutExtension }.orEmpty()

private fun VirtualFile.getRootAndRelativePath(project: Project): List<String> =
    path.removePrefix("${project.basePath}/${MainTaskTrackerConfig.PLUGIN_NAME}/${extensionEnum().name.lowercase()}/")
        .removeSuffix("/$name").split("/")

private fun VirtualFile.extensionEnum() =
    Extension.values().find { it.ext == ".$extension" } ?: error("Unexpected extension `$extension`")
