package org.jetBrains.research.tasktracker.tracking

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetBrains.research.tasktracker.models.Extension
import org.jetBrains.research.tasktracker.tracking.task.Task
import java.io.File
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertFailsWith

class TaskFileHandlerTest : BasePlatformTestCase() {

    private lateinit var taskFileHandler: TaskFileHandler

    private fun getPrivateFunc(name: String): KFunction<*> {
        val method = taskFileHandler::class.memberFunctions.find { it.name == name }
            ?: error("Function hasn't been find")
        method.isAccessible = true
        return method
    }

    override fun setUp() {
        super.setUp()
        taskFileHandler = TaskFileHandler
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
        assert(file1.path == "${project.basePath}/${PLUGIN_NAME}/cpp/task1.cpp")
        assert(file2.path == "${project.basePath}/${PLUGIN_NAME}/tasks/task2.kt")
        assert(String(file1.contentsToByteArray()) == task1.getContent())
        assert(String(file2.contentsToByteArray()) == DefaultContentProvider.getDefaultContent(task2))

    }

    private val task1 = object : Task {
        override fun getContent(): String {
            return "int main(){return 0;}"
        }

        override fun getRelativeFilePath(): String? {
            return null
        }

        override fun getFileName(): String {
            return "task1"
        }

        override fun getExtension(): Extension {
            return Extension.CPP
        }
    }

    private val task2 = object : Task {
        override fun getContent(): String? {
            return null
        }

        override fun getRelativeFilePath(): String {
            return "tasks"
        }

        override fun getFileName(): String {
            return "task2"
        }

        override fun getExtension(): Extension {
            return Extension.KOTLIN
        }
    }

}
