@file:Suppress("NoWildcardImports")

package org.jetbrains.research.tasktracker.tracking

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetbrains.research.tasktracker.config.content.task.base.Task
import org.jetbrains.research.tasktracker.config.content.task.base.TaskWithFiles
import org.jetbrains.research.tasktracker.tracking.mock.*
import kotlin.test.assertFailsWith

class TaskFileHandlerTest : BasePlatformTestCase() {

    private val projectToTaskToFiles = TaskFileHandler.projectToTaskToFiles

    override fun tearDown() {
        tasks.forEach { task ->
            TaskFileHandler.disposeTask(project, task)
        }
        super.tearDown()
    }

    fun testDisposeNonExistedTask() {
        assertNoThrowable {
            TaskFileHandler.disposeTask(project, task1)
        }
    }

    fun testInitSingleTask() {
        testInitTasks(listOf(task1))
    }

    fun testInitMultipleTask() {
        testInitTasks(tasks)
    }

    fun testInitExistingTask() {
        TaskFileHandler.initTask(project, task1)
        assertFailsWith<Throwable>("Expected Throwable on second initialization of $task1") {
            TaskFileHandler.initTask(project, task1)
        }
    }

    fun testDisposeSingleTask() {
        testDisposeTasks(listOf(task1))
    }

    fun testDisposeMultipleTask() {
        testDisposeTasks(tasks)
    }

    private fun testInitTasks(tasks: List<TaskWithFiles>) {
        assertNoThrowable {
            tasks.forEach { task ->
                TaskFileHandler.initTask(project, task)
            }
        }
        assert(projectToTaskToFiles.size == 1) {
            "Expected the size of 'projectToTaskToFiles' to be 1, but found ${projectToTaskToFiles.size}"
        }
        assertNotNull(projectToTaskToFiles[project])
        val error = buildString {
            append("Expected the size of 'projectToTaskToFiles[project]' to be ${tasks.size}, ")
            append("but found ${projectToTaskToFiles[project]?.size}")
        }
        assert(projectToTaskToFiles[project]?.let { it.size == tasks.size } ?: false) { error }

        tasks.forEach { task ->
            assert(
                projectToTaskToFiles[project]?.contains(task) ?: false
            ) {
                "projectToTaskToFiles[project] must contain $task"
            }
        }
        tasks.forEach { task ->
            val virtualFileTask = task.files.map { taskFile ->
                "${project.basePath}/$PLUGIN_NAME/${relativePaths[taskFile]}".getVirtualFile()
            }.toMockTask(project)
            assert(
                virtualFileTask == task
            ) { "Incorrect task $virtualFileTask from VirtualFile" }
        }
    }

    private fun testDisposeTasks(tasks: List<Task>) {
        tasks.forEach { task ->
            TaskFileHandler.initTask(project, task)
        }
        assertNoThrowable {
            tasks.forEach { task ->
                TaskFileHandler.disposeTask(project, task)
            }
        }
        assert(projectToTaskToFiles.isEmpty()) { "projectToTaskToFiles expected to be empty" }
    }

    companion object {
        private val relativePaths =
            mapOf(
                taskFile1 to "cpp/src/task1/task1.cpp",
                taskFile2Kotlin to "kotlin/tasks/src/task2/task2.kt",
                taskFile2Java to "java/tasks/src/task2/java/task2.java",
                taskFile3 to "python/example/src/task3/task3.py",
                taskFile4 to "jupyter/src/task4/jupyter/task4.ipynb",
            )
        private val tasks = listOf(task1, task2, task3, task4)
    }
}
