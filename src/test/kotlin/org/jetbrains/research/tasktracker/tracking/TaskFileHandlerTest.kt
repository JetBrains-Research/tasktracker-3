package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.mock.MockTask
import org.jetbrains.research.tasktracker.tracking.mock.task1
import org.jetbrains.research.tasktracker.tracking.mock.task2
import java.io.File
import kotlin.test.assertFailsWith

class TaskFileHandlerTest : BasePlatformTestCase() {

    private val projectToTaskToFiles = TaskFileHandler.projectToTaskToFiles

    override fun tearDown() {
        TaskFileHandler.disposeTask(project, task1)
        TaskFileHandler.disposeTask(project, task2)
        super.tearDown()
    }

    fun testDisposeNonExistedTask() {
        assertNoThrowable {
            TaskFileHandler.disposeTask(project, task1)
        }
    }

    fun testInitSingleTask() {
        assertNoThrowable {
            TaskFileHandler.initTask(project, task1)
        }
        assert(projectToTaskToFiles.size == 1) {
            "Expected the size of 'projectToTaskToFiles' to be 1, but found ${projectToTaskToFiles.size}"
        }
        assertNotNull(projectToTaskToFiles[project])
        assert(
            projectToTaskToFiles[project]?.let { it.size == 1 }
                ?: false
        ) {
            "Expected the size of 'projectToTaskToFiles[project]'" +
                " to be 1, but found ${projectToTaskToFiles[project]?.size}"
        }
        assert(
            projectToTaskToFiles[project]?.contains(task1) ?: false
        ) {
            "projectToTaskToFiles[project] must contain $task1"
        }
        val virtualFileTask1 = "${project.basePath}/$PLUGIN_NAME/cpp/task1.cpp".getVirtualFile().toMockTask()
        assert(
            virtualFileTask1 == task1
        ) { "Incorrect task $virtualFileTask1 from VirtualFile" }
    }

    fun testInitMultipleTask() {
        assertNoThrowable {
            TaskFileHandler.initTask(project, task1)
            TaskFileHandler.initTask(project, task2)
        }
        assert(projectToTaskToFiles.size == 1) {
            "Expected the size of 'projectToTaskToFiles' to be 1," +
                " but found ${projectToTaskToFiles.size}"
        }
        assertNotNull(projectToTaskToFiles[project])
        assert(
            projectToTaskToFiles[project]?.let { it.size == 2 } ?: false
        ) {
            "Expected the size of 'projectToTaskToFiles[project]'" +
                " to be 2, but found ${projectToTaskToFiles[project]?.size}"
        }
        assert(
            projectToTaskToFiles[project]?.contains(task1) ?: false
        ) { "projectToTaskToFiles[project] must contain task1" }
        assert(
            projectToTaskToFiles[project]?.contains(task2) ?: false
        ) { "projectToTaskToFiles[project] must contain task1" }
        val virtualFileTask1 = "${project.basePath}/$PLUGIN_NAME/cpp/task1.cpp".getVirtualFile().toMockTask()
        val virtualFileTask2 = "${project.basePath}/$PLUGIN_NAME/tasks/task2.kt".getVirtualFile().toMockTask()
        assert(
            virtualFileTask1 == task1
        ) { "Incorrect task $virtualFileTask1 from VirtualFile" }
        assert(
            virtualFileTask2 == task2
        ) { "Incorrect task $virtualFileTask2 from VirtualFile" }
    }

    fun testInitExistingTask() {
        TaskFileHandler.initTask(project, task1)
        assertFailsWith<Throwable>("Expected Throwable on second initialization of $task1") {
            TaskFileHandler.initTask(project, task1)
        }
    }

    fun testDisposeSingleTask() {
        TaskFileHandler.initTask(project, task1)
        assertNoThrowable {
            TaskFileHandler.disposeTask(project, task1)
        }
        assert(projectToTaskToFiles.isEmpty()) { "projectToTaskToFiles expected to be empty" }
    }

    fun testDisposeMultipleTask() {
        TaskFileHandler.initTask(project, task1)
        TaskFileHandler.initTask(project, task2)
        assertNoThrowable {
            TaskFileHandler.disposeTask(project, task1)
        }
        assert(
            projectToTaskToFiles[project]?.let { it.size == 1 } ?: false
        ) {
            "Expected the size of 'projectToTaskToFiles' to be 1, but found ${projectToTaskToFiles.size}"
        }
        assertNoThrowable {
            TaskFileHandler.disposeTask(project, task2)
        }
        assert(projectToTaskToFiles.isEmpty()) { "projectToTaskToFiles expected to be empty" }
    }

    fun testTaskDocumentListener() {
        val directory = File("${project.basePath}/$PLUGIN_NAME/cpp/")
        directory.mkdirs()
        val virtualFile = "${directory.path}/task.cpp".getVirtualFile()
        val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            ?: error("document with path ${virtualFile.path} must exist")
        val logFileName =
            "${virtualFile.nameWithoutExtension}_${virtualFile.hashCode()}_${document.hashCode()}_0.csv"
        val logFile = File("${MainTaskTrackerConfig.pluginFolderPath}/$logFileName")
        assert(!logFile.exists()) {
            "log file with path ${logFile.path} should be created on first event in TaskDocumentListener"
        }
        document.addDocumentListener(TaskDocumentListener())
        ApplicationManager.getApplication().runWriteAction {
            document.setText("test")
        }
        assert(logFile.exists()) { "log file with path ${logFile.path} should have been created" }
    }

    private fun String.getVirtualFile(): VirtualFile {
        val file = File(this)
        ApplicationManager.getApplication().runWriteAction {
            if (file.isDirectory) {
                file.mkdirs()
            } else {
                file.createNewFile()
            }
        }
        return LocalFileSystem.getInstance().findFileByIoFile(file) ?: error("File $this must exist")
    }

    private fun VirtualFile.toMockTask(): MockTask {
        return MockTask(
            nameWithoutExtension,
            Extension.values().find { it.ext == ".$extension" } ?: error("Unexpected extension"),
            FileDocumentManager.getInstance().getDocument(this)?.text,
            path.removePrefix("${project.basePath}/$PLUGIN_NAME/").removeSuffix("/$name")
        )
    }
}
