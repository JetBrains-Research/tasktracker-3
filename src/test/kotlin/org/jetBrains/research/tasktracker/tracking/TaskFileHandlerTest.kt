package org.jetBrains.research.tasktracker.tracking

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetBrains.research.tasktracker.models.Extension
import org.jetBrains.research.tasktracker.tracking.task.Task
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
            "projectToTaskToFiles[project] must contain task1"
        }
        assert(
            "${project.basePath}/$PLUGIN_NAME/cpp/task1.cpp".getVirtualFile().toMockTask() == task1
        ) { "Incorrect task from VirtualFile" }
        // TODO check listener has been added
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
        assert(
            "${project.basePath}/$PLUGIN_NAME/cpp/task1.cpp".getVirtualFile().toMockTask() == task1
        ) { "Incorrect task from VirtualFile" }
        assert(
            "${project.basePath}/$PLUGIN_NAME/tasks/task2.kt".getVirtualFile().toMockTask() == task2
        ) { "Incorrect task from VirtualFile" }
    }

    fun testInitExistingTask() {
        TaskFileHandler.initTask(project, task1)
        assertFailsWith<Throwable> {
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

    companion object {
        val task1 = MockTask("task1", Extension.CPP, "int main(){return 0;}")
        val task2 = MockTask("task2", Extension.KOTLIN, relativePath = "tasks")
    }

    data class MockTask(
        val filename: String,
        val ext: Extension,
        val text: String? = null,
        val relativePath: String? = null
    ) : Task {
        override fun getContent() = text

        override fun getRelativeFilePath() = relativePath

        override fun getFileName() = filename

        override fun getExtension() = ext

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MockTask

            if (filename != other.filename) return false
            if (ext != other.ext) return false
            if (text != other.text) {
                if (text == null || other.text == null) {
                    return text == DefaultContentProvider.getDefaultContent(this) ||
                        other.text == DefaultContentProvider.getDefaultContent(other)
                }
            }
            return true
        }
    }

    private fun VirtualFile.toMockTask(): MockTask {
        return MockTask(
            nameWithoutExtension,
            Extension.values().find { it.ext == ".$extension" } ?: error("Unexpected extension"),
            FileDocumentManager.getInstance().getDocument(this)?.text,
            null
        )
    }
}
