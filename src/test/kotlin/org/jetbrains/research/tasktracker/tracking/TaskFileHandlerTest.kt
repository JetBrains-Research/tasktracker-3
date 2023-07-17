package org.jetbrains.research.tasktracker.tracking

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetbrains.research.tasktracker.tracking.mock.task1
import org.jetbrains.research.tasktracker.tracking.mock.task2
import org.jetbrains.research.tasktracker.tracking.mock.task3
import org.jetbrains.research.tasktracker.tracking.mock.task4
import org.jetbrains.research.tasktracker.tracking.mock.task5
import org.jetbrains.research.tasktracker.tracking.mock.toMockTask
import org.jetbrains.research.tasktracker.tracking.task.Task
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

    private fun testInitTasks(tasks: List<Task>) {
        assertNoThrowable {
            tasks.forEach { task ->
                TaskFileHandler.initTask(project, task)
            }
        }
        assert(projectToTaskToFiles.size == 1) {
            "Expected the size of 'projectToTaskToFiles' to be 1, but found ${projectToTaskToFiles.size}"
        }
        assertNotNull(projectToTaskToFiles[project])
        assert(projectToTaskToFiles[project]?.let { it.size == tasks.size } ?: false) {
            "Expected the size of 'projectToTaskToFiles[project]'" +
                " to be ${tasks.size}, but found ${projectToTaskToFiles[project]?.size}"
        }

        tasks.forEach { task ->
            assert(
                projectToTaskToFiles[project]?.contains(task) ?: false
            ) {
                "projectToTaskToFiles[project] must contain $task"
            }
        }
        tasks.forEach { task ->
            val virtualFileTask =
                "${project.basePath}/$PLUGIN_NAME/${relativePaths[task]}".getVirtualFile().toMockTask(project)
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
                task1 to "cpp/task1.cpp",
                task2 to "kotlin/tasks/task2.kt",
                task3 to "java/task3.java",
                task4 to "python/tasks/task4.py",
                task5 to "jupyter/task5.ipynb"
            )
        private val tasks = listOf(task1, task2, task3, task4, task5)
    }
}
