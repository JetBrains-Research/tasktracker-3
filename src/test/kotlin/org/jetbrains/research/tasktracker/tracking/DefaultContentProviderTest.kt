package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.config.content.task.base.ITaskFileInfo
import org.jetbrains.research.tasktracker.tracking.mock.task1
import org.jetbrains.research.tasktracker.tracking.mock.task2
import org.jetbrains.research.tasktracker.tracking.mock.task3
import org.jetbrains.research.tasktracker.tracking.mock.task4
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultContentProviderTest {

    private fun ITaskFileInfo.loadContent() = DefaultContentProviderTest::class.java
        .getResource("$filename${extension?.ext ?: ""}")
        ?.readText()?.trimIndent() ?: error("Cannot find $filename${extension?.ext ?: ""} file")

    @Test
    fun getDefaultContentTest() {
        listOf(task1, task2, task3, task4).forEach { task ->
            task.files.forEach { taskFile ->
                assertEquals(
                    "Unexpected default content for `$taskFile` task file",
                    taskFile.loadContent(),
                    DefaultContentProvider.getDefaultContent(
                        taskFile.extension,
                        taskFile.relativePath
                    )
                )
            }
        }
    }
}
