package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.jps.model.serialization.PathMacroUtil
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetbrains.research.tasktracker.tracking.task.Task
import org.jetbrains.research.tasktracker.tracking.task.TaskFile
import java.io.File
import java.util.*
import kotlin.collections.HashMap

@Suppress("UnusedPrivateMember")
object TaskFileHandler {
    private val logger: Logger = Logger.getInstance(javaClass)
    private val listener by lazy { TaskDocumentListener() }
    val projectToTaskToFiles: MutableMap<Project, MutableMap<Task, MutableList<VirtualFile>>> = HashMap()

    fun initProject(project: Project) {
        TODO()
    }

    fun initTask(project: Project, task: Task) {
        getOrCreateFiles(project, task).forEach { file ->
            file?.let {
                addVirtualFileListener(it)
                projectToTaskToFiles.putIfAbsent(project, mutableMapOf())
                projectToTaskToFiles[project]?.putIfAbsent(task, mutableListOf())
                projectToTaskToFiles[project]?.get(task)?.add(it)
            }
        }
    }

    // TODO not forget to remove from document loggers hashmap
    fun disposeTask(project: Project, task: Task) {
        projectToTaskToFiles[project]?.let {
            it[task]?.let { virtualFiles -> removeVirtualFileListener(virtualFiles) }
                ?: logger.warn("attempt to dispose a uninitialized task: '$task'")
            it.remove(task)
            if (it.isEmpty()) {
                projectToTaskToFiles.remove(project)
            }
        } ?: logger.warn("attempt to dispose task: '$task' from uninitialized project: '$project'")
    }

    private fun addVirtualFileListener(virtualFile: VirtualFile) {
        ApplicationManager.getApplication().invokeAndWait {
            val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            document?.addDocumentListener(listener)
                ?: logger.warn("attempt to add listener for non-existing document: '$document'")
        }
    }

    private fun removeVirtualFileListener(virtualFiles: List<VirtualFile>) {
        virtualFiles.forEach { file ->
            ApplicationManager.getApplication().invokeAndWait {
                val document = FileDocumentManager.getInstance().getDocument(file)
                document?.removeDocumentListener(listener)
            }
        }
    }

    private fun getOrCreateFiles(project: Project, task: Task): List<VirtualFile?> {
        return task.taskFiles.map { taskFile ->
            val relativeFilePath = taskFile.relativePath
            ApplicationManager.getApplication().runWriteAction {
                addSourceFolder(relativeFilePath, ModuleManager.getInstance(project).modules.last())
            }
            val path = getPath(project, taskFile, task)
            val file = File(path)
            if (!file.exists()) {
                ApplicationManager.getApplication().runWriteAction {
                    FileUtil.createIfDoesntExist(file)
                    file.writeText(
                        taskFile.content ?: DefaultContentProvider.getDefaultContent(
                            taskFile.extension,
                            taskFile.relativePath
                        )
                    )
                }
            }
            LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
        }
    }

    private fun getPath(project: Project, taskFile: TaskFile, task: Task): String {
        return "${project.basePath}/$PLUGIN_NAME/${taskFile.extension.name.lowercase(Locale.getDefault())}" +
            "${task.root.pathOrEmpty()}/${taskFile.sourceSet.path}/${task.name}" +
            "${taskFile.relativePath.pathOrEmpty()}/${taskFile.filename}${taskFile.extension.ext}"
    }

    private fun addSourceFolder(relativePath: String, module: Module) {
        val directory = File(PathMacroUtil.getModuleDir(module.moduleFilePath), relativePath)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(directory)
        virtualFile?.let {
            val rootModel = ModuleRootManager.getInstance(module).modifiableModel
            getContentEntry(virtualFile, rootModel)?.addSourceFolder(it.url, false)
            rootModel.commit()
        }
    }

    private fun getContentEntry(url: VirtualFile?, rootModel: ModifiableRootModel): ContentEntry? {
        rootModel.contentEntries.forEach { e ->
            url?.let {
                if (VfsUtilCore.isEqualOrAncestor(e.url, url.url)) {
                    return e
                }
            }
        }
        return null
    }

    private fun String.pathOrEmpty() = if (!isEmpty()) "/$this" else this
}
