package org.jetBrains.research.tasktracker.tracking

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetBrains.research.tasktracker.models.Extension
import org.jetBrains.research.tasktracker.tracking.task.Task
import org.junit.Test
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.isAccessible

class TaskFileHandlerTest : BasePlatformTestCase() {

    private lateinit var taskFileHandler: TaskFileHandler

    override fun setUp() {
        super.setUp()
        taskFileHandler = TaskFileHandler
    }

    fun testGetOrCreateFile() {
        val method = taskFileHandler::class.memberFunctions.find { it.name == "getOrCreateFile" }
            ?: error("Function hasn't been find")
        method.isAccessible = true
        val file1 = method.call(taskFileHandler, this.project, task1) as VirtualFile? ?: error("file must exist")
        val file2 = method.call(taskFileHandler, this.project, task2) as VirtualFile? ?: error("file must exist")
        assert(file1.exists())
        assert(file2.exists())
        assert(file1.path == "${this.project.basePath}/${PLUGIN_NAME}/cpp/task1.cpp")
        assert(file2.path == "${this.project.basePath}/${PLUGIN_NAME}/tasks/task2.kt")
        assert(String(file1.contentsToByteArray()) == task1.getContent())
        assert(String(file2.contentsToByteArray()) == DefaultContentProvider.getDefaultContent(task2))
    }

    private val task1 = object : Task {
        override fun getContent(): String? {
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

        override fun getRelativeFilePath(): String? {
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
