package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetbrains.research.tasktracker.tracking.mock.task1
import org.jetbrains.research.tasktracker.tracking.mock.task2
import java.io.File
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertFailsWith

class TaskFileHandlerTest : BasePlatformTestCase() {

    private lateinit var taskFileHandler: TaskFileHandler

    private fun getPrivateFunc(name: String): KFunction<*> {
        val method = TaskFileHandler::class.memberFunctions.find { it.name == name }
            ?: error("Function hasn't been found")
        method.isAccessible = true
        return method
    }

    override fun setUp() {
        super.setUp()
        taskFileHandler = TaskFileHandler
    }

    fun testInitDisposeTask() {
        val projectToTaskToFiles =
            taskFileHandler.javaClass.getDeclaredField("projectToTaskToFiles").also { it.isAccessible = true }
                .get(taskFileHandler) as MutableMap<*, MutableMap<*, *>>
        assert(projectToTaskToFiles.isEmpty())

        assertNoThrowable {
            taskFileHandler.disposeTask(project, task1)
            taskFileHandler.initTask(project, task1)
        }
        assert(projectToTaskToFiles.size == 1)
        assert(projectToTaskToFiles[project]?.let { it.size == 1 } ?: false)
        assertNoThrowable {
            taskFileHandler.initTask(project, task2)
        }
        assert(projectToTaskToFiles.size == 1)
        assert(projectToTaskToFiles[project]?.let { it.size == 2 } ?: false)
        assertFailsWith<Throwable> {
            taskFileHandler.initTask(project, task2)
        }
        assert(projectToTaskToFiles[project]?.let { it.size == 2 } ?: false)
        assertNoThrowable {
            taskFileHandler.disposeTask(project, task1)
        }
        assert(projectToTaskToFiles[project]?.let { it.size == 1 } ?: false)
        assert(projectToTaskToFiles[project]?.let { it.keys.first() == task2 } ?: false)
        assertNoThrowable {
            taskFileHandler.disposeTask(project, task2)
        }
        assert(projectToTaskToFiles.isEmpty())
    }

    fun testAddVirtualFileListener() {
        val file1 = File("${project.basePath}/test1")
        val file2 = File("${project.basePath}/test2")
        val directory = File("${project.basePath}/tests/")
        ApplicationManager.getApplication().runWriteAction {
            FileUtil.createIfDoesntExist(file1)
            FileUtil.createIfDoesntExist(file2)
            FileUtil.createDirectory(directory)
        }
        val virtualFile1 = LocalFileSystem.getInstance().findFileByIoFile(file1) ?: error("file must exist")
        val virtualFile2 = LocalFileSystem.getInstance().findFileByIoFile(file2) ?: error("file must exist")
        val virtualDirectory =
            LocalFileSystem.getInstance().findFileByIoFile(directory) ?: error("file must exist")
        assert(virtualFile1.exists())
        assert(virtualFile2.exists())
        assert(virtualDirectory.exists())
        val method = getPrivateFunc("addVirtualFileListener")
        assertNoThrowable {
            method.call(taskFileHandler, virtualFile1)
            method.call(taskFileHandler, virtualFile2)
            method.call(taskFileHandler, virtualDirectory)
        }
    }

    fun testRemoveVirtualFileListener() {
        val file = File("${project.basePath}/test")
        val directory = File("${project.basePath}/tests/")
        ApplicationManager.getApplication().runWriteAction {
            FileUtil.createIfDoesntExist(file)
            FileUtil.createDirectory(directory)
        }
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file) ?: error("file must exist")
        val virtualDirectory =
            LocalFileSystem.getInstance().findFileByIoFile(directory) ?: error("file must exist")
        assert(virtualFile.exists())
        assert(virtualDirectory.exists())
        val methodRemove = getPrivateFunc("removeVirtualFileListener")
        assertFailsWith<Throwable> {
            methodRemove.call(taskFileHandler, virtualFile)
        }
        val methodAdd = getPrivateFunc("addVirtualFileListener")
        assertNoThrowable {
            methodAdd.call(taskFileHandler, virtualFile)
            methodRemove.call(taskFileHandler, virtualFile)
            methodRemove.call(taskFileHandler, virtualDirectory)
        }
        assertFailsWith<Throwable> {
            methodRemove.call(taskFileHandler, virtualFile)
        }
    }

    fun testGetOrCreateFile() {
        val method = getPrivateFunc("getOrCreateFile")
        val file1 = method.call(taskFileHandler, project, task1) as VirtualFile? ?: error("file must exist")
        val file2 = method.call(taskFileHandler, project, task2) as VirtualFile? ?: error("file must exist")
        assert(file1.exists())
        assert(file2.exists())
        println(file1.path)
        println("${project.basePath}/$PLUGIN_NAME/cpp/task1.cpp")
        assert(file1.path == "${project.basePath}/$PLUGIN_NAME/cpp/task1.cpp")
        assert(file2.path == "${project.basePath}/$PLUGIN_NAME/tasks/task2.kt")
        assert(String(file1.contentsToByteArray()) == task1.content)
        assert(String(file2.contentsToByteArray()) == DefaultContentProvider.getDefaultContent(task2))
    }
}
