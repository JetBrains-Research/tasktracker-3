package org.jetBrains.research.tasktracker.tracking

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
import org.jetBrains.research.tasktracker.models.Extension
import org.jetbrains.jps.model.serialization.PathMacroUtil
import java.io.File

// TODO delete, just template
interface Task {
    fun getContent(): String
    fun getRelativeFilePath(): String
    fun getFileName(): String
}

object TaskFileHandler {
    private val logger: Logger = Logger.getInstance(javaClass)
    private val listener by lazy { TaskDocumentListener() }

    fun initProject(project: Project) {
        TODO()
    }

    // TODO Control which files are listening
    fun listenVirtualFile(virtualFile: VirtualFile) {
        ApplicationManager.getApplication().invokeAndWait {
            val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            document?.addDocumentListener(listener)
        }
    }

    // TODO fix path and content to file
    private fun getOrCreateFile(project: Project, task: Task, extension: Extension): VirtualFile? {
        val relativeFilePath = task.getRelativeFilePath()
        ApplicationManager.getApplication().runWriteAction {
            addSourceFolder(relativeFilePath, ModuleManager.getInstance(project).modules.last())
        }
        val file =
            File("${project.basePath}/$relativeFilePath/${task.getFileName()}/${extension.ext}")
        if (!file.exists()) {
            ApplicationManager.getApplication().runWriteAction {
                FileUtil.createIfDoesntExist(file)
                file.writeText(task.getContent())
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
            url?.let { if (VfsUtilCore.isEqualOrAncestor(e.url, url.url)) return e }
        }
        return null
    }
}
