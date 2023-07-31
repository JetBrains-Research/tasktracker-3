package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.tracking.mock.task1
import org.jetbrains.research.tasktracker.tracking.mock.task2
import org.jetbrains.research.tasktracker.tracking.mock.task3
import org.jetbrains.research.tasktracker.tracking.mock.task4
import org.jetbrains.research.tasktracker.tracking.task.TaskFile
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultContentProviderTest {

    private fun TaskFile.loadContent() = DefaultContentProviderTest::class.java
        .getResource("$filename${extension.ext}")
        ?.readText()?.trimIndent() ?: error("Cannot find $filename file")

    @Test
    fun getDefaultContentTest() {
        listOf(task1, task2, task3, task4).forEach { task ->
            task.taskFiles.forEach { taskFile ->
                assertEquals(
                    "Unexpected default content for `$taskFile` task file",
                    taskFile.loadContent(),
                    DefaultContentProvider.getDefaultContent(
                        taskFile.extension,
                        "${task.root}/${taskFile.relativePath}"
                    )
                )
            }
        }
    }
}
