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
import org.jetbrains.research.tasktracker.tracking.task.Task
import java.io.File

@Suppress("UnusedPrivateMember")
object TaskFileHandler {
    private val logger: Logger = Logger.getInstance(javaClass)
    private val listener by lazy { TaskDocumentListener() }
    val projectToTaskToFiles: MutableMap<Project, MutableMap<Task, VirtualFile>> = HashMap()

    fun initProject(project: Project) {
        TODO()
    }

    fun initTask(project: Project, task: Task) {
        getOrCreateFile(project, task)?.let {
            addVirtualFileListener(it)
            projectToTaskToFiles.putIfAbsent(project, mutableMapOf())
            projectToTaskToFiles[project]?.set(task, it)
        }
    }

    // TODO not forget to remove from document loggers hashmap
    fun disposeTask(project: Project, task: Task) {
        projectToTaskToFiles[project]?.let {
            it[task]?.let { virtualFile -> removeVirtualFileListener(virtualFile) }
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

    private fun removeVirtualFileListener(virtualFile: VirtualFile) {
        ApplicationManager.getApplication().invokeAndWait {
            val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            document?.removeDocumentListener(listener)
        }
    }

    private fun getOrCreateFile(project: Project, task: Task): VirtualFile? {
        val relativeFilePath =
            "${DefaultContentProvider.getDefaultFolderRelativePath(task)}/${task.relativeFilePath ?: ""}"
        ApplicationManager.getApplication().runWriteAction {
            addSourceFolder(relativeFilePath, ModuleManager.getInstance(project).modules.last())
        }
        val file = File("${project.basePath}/$relativeFilePath/${task.filename}${task.extension.ext}")
        if (!file.exists()) {
            ApplicationManager.getApplication().runWriteAction {
                FileUtil.createIfDoesntExist(file)
                file.writeText(task.content ?: DefaultContentProvider.getDefaultContent(task))
            }
        }
        return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
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
}
