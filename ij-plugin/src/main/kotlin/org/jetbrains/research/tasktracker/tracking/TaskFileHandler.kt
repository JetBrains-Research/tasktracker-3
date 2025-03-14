package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.event.DocumentListener
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
import org.jetbrains.research.tasktracker.config.content.task.base.ITaskFileInfo
import org.jetbrains.research.tasktracker.config.content.task.base.Task
import org.jetbrains.research.tasktracker.config.content.task.base.TaskWithFiles
import org.jetbrains.research.tasktracker.tracking.logger.DocumentLogger
import org.jetbrains.research.tasktracker.tracking.task.SourceSet
import java.io.File

typealias ProjectTaskFileMap = MutableMap<Project, MutableMap<Task, MutableList<VirtualFile>>>
typealias ProjectTaskIdFile = MutableMap<Project, MutableMap<Task, MutableMap<String, VirtualFile>>>

@Suppress("UnusedPrivateMember", "TooManyFunctions")
object TaskFileHandler {
    private val logger: Logger = Logger.getInstance(TaskFileHandler.javaClass)
    private var listener: DocumentListener? = null
    private val projectTaskIdToFile: ProjectTaskIdFile = HashMap()
    val projectToTaskToFiles: ProjectTaskFileMap = HashMap()

    fun initProject(project: Project) {
        listener = TaskDocumentListener(project)
    }

    private fun getListener() = listener ?: error("Listener is not define")

    fun initTask(project: Project, task: Task) {
        projectTaskIdToFile.putIfAbsent(project, mutableMapOf())
        projectTaskIdToFile[project]?.putIfAbsent(task, mutableMapOf())
        getOrCreateFiles(project, task).forEach { file -> // TODO sometimes problem with null array
            file?.let {
                addVirtualFileListener(project, it)
                projectToTaskToFiles.putIfAbsent(project, mutableMapOf())
                projectToTaskToFiles[project]?.putIfAbsent(task, mutableListOf())
                projectToTaskToFiles[project]?.get(task)?.add(it)
            }
        }
    }

    fun disposeAllTasks() {
        projectToTaskToFiles.forEach { (project, projectFiles) ->
            projectFiles.keys.forEach {
                disposeTask(project, it)
            }
        }
    }

    // TODO not forget to remove from document loggers hashmap, flush data
    fun disposeTask(project: Project, task: Task) {
        projectToTaskToFiles[project]?.let {
            it[task]?.let { virtualFiles -> removeVirtualFileListener(project, virtualFiles) }
                ?: logger.warn("attempt to dispose a uninitialized task: '$task'")
            it.remove(task)
            projectTaskIdToFile[project]?.remove(task)
            if (it.isEmpty()) {
                projectToTaskToFiles.remove(project)
                projectTaskIdToFile.remove(project)
            }
        } ?: logger.warn("attempt to dispose task: '$task' from uninitialized project: '$project'")
    }

    private fun addVirtualFileListener(project: Project, virtualFile: VirtualFile) {
        ApplicationManager.getApplication().runWriteAction {
            val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            document?.let {
                it.addDocumentListener(getListener())
                // Log the first state
                DocumentLogger.log(project, it)
            } ?: logger.warn("attempt to add listener for non-existing document: '$document'")
        }
    }

    private fun removeVirtualFileListener(project: Project, virtualFiles: List<VirtualFile>) {
        virtualFiles.forEach { file ->
            ApplicationManager.getApplication().runWriteAction {
                val document = FileDocumentManager.getInstance().getDocument(file)
                document?.let {
                    DocumentLogger.removeDocumentLogPrinter(project, document)
                    document.removeDocumentListener(getListener())
                }
            }
        }
    }

    // TODO group tasks by sourceSet and make sourceSet once for each
    private fun getOrCreateFiles(project: Project, task: Task): List<VirtualFile?> = when (task) {
        is TaskWithFiles -> {
            val files = task.files.map { taskFile ->
                val path = getPath(project, taskFile, task)
                val file = File(path)
                if (!file.exists()) {
                    file.writeDefaultContent(taskFile, task.name)
                }
                LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)?.also {
                    taskFile.id?.let { id ->
                        projectTaskIdToFile[project]?.get(task)?.putIfAbsent(id, it)
                    }
                }
            }
            addSourceFolders(project, task)
            files
        }

        else -> emptyList()
    }

    private fun addSourceFolders(project: Project, task: TaskWithFiles) {
        val sourceFolders = task.files.map { Pair(it.extension, it.sourceSet) }.toSet().map {
            "$PLUGIN_NAME/${it.first?.getDirectoryName()}/${task.root.pathOrEmpty()}/${it.second.path}"
        }
        sourceFolders.forEach {
            ApplicationManager.getApplication().runWriteAction {
                addSourceFolder(ModuleManager.getInstance(project).modules.last(), it) // TODO array is empty
            }
        }
    }

    private fun File.writeDefaultContent(taskFile: ITaskFileInfo, name: String) {
        if (!exists()) {
            ApplicationManager.getApplication().runWriteAction {
                FileUtil.createParentDirs(this)
                writeText(
                    taskFile.content ?: DefaultContentProvider.getDefaultContent(
                        taskFile.extension,
                        "$name/${taskFile.relativePath}"
                    )
                )
            }
        }
    }

    private fun getPath(project: Project, taskFile: ITaskFileInfo, task: TaskWithFiles): String = buildString {
        append("${project.basePath}")
        if (taskFile.isInternal) {
            append("/$PLUGIN_NAME/${taskFile.extension?.getDirectoryName() ?: ""}")
            append("${task.root.pathOrEmpty()}/${taskFile.sourceSet.path}")
        }
        append("${taskFile.relativePath.pathOrEmpty()}/")
        append("${taskFile.filename}${taskFile.extension?.ext ?: ""}")
    }

    private fun addSourceFolder(module: Module, relativePath: String) {
        val directory = File(PathMacroUtil.getModuleDir(module.moduleFilePath), relativePath)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(directory)
        virtualFile?.let {
            val rootModel = ModuleRootManager.getInstance(module).modifiableModel
            getContentEntry(virtualFile, rootModel)?.addSourceFolder(it.url, relativePath.endsWith(SourceSet.TEST.path))
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

    private fun String.pathOrEmpty() = if (isNotEmpty()) {
        if (this.startsWith("/")) this else "/$this"
    } else {
        this
    }.replace(" ", "")

    fun getVirtualFileByProjectTaskId(project: Project, task: Task, id: String) =
        projectTaskIdToFile[project]?.get(task)?.get(id)
}
