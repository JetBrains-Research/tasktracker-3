package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.mock.task1
import org.jetbrains.research.tasktracker.tracking.mock.task2
import org.jetbrains.research.tasktracker.tracking.mock.task3
import org.jetbrains.research.tasktracker.tracking.mock.task4
import org.jetbrains.research.tasktracker.tracking.mock.task5
import org.jetbrains.research.tasktracker.tracking.task.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DefaultContentProviderTest {

    private fun Extension.getExpectedFolderName(): String = this.name.lowercase(Locale.getDefault())

    private fun Task.loadContent() = DefaultContentProviderTest::class.java
        .getResource("$filename${extension.ext}")
        ?.readText()?.trimIndent() ?: error("Cannot find $filename file")

    @Test
    fun getDefaultFolderRelativePathTest() {
        listOf(task1, task2, task3, task4, task5).forEach { task ->
            assertEquals(
                "Unexpected default path for $task task",
                "$PLUGIN_NAME/${task.extension.getExpectedFolderName()}",
                DefaultContentProvider.getDefaultFolderRelativePath(task)
            )
        }
    }

    @Test
    fun getDefaultContentTest() {
        listOf(task1, task2, task3, task4, task5).forEach { task ->
            assertEquals(
                "Unexpected default content for $task task",
                task.loadContent(),
                DefaultContentProvider.getDefaultContent(task)
            )
        }
    }
}
