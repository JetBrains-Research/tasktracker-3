package org.jetBrains.research.tasktracker.tracking

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetBrains.research.tasktracker.models.Extension
import org.jetBrains.research.tasktracker.tracking.task.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DefaultContentProviderTest {

    private fun Extension.getExpectedFolderName(): String = this.name.lowercase(Locale.getDefault())

    private fun Task.loadContent() = javaClass.classLoader
        .getResource("tracking/${this.getFileName()}${getExtension().ext}")
        ?.readText()?.trimIndent() ?: error("Cannot find ${getFileName()} file")

    @Test
    fun getDefaultFolderRelativePathTest() {
        listOf(task1, task2, task3, task4, task5).forEach { task ->
            assertEquals(
                "Unexpected default path",
                "$PLUGIN_NAME/${task.getExtension().getExpectedFolderName()}",
                DefaultContentProvider.getDefaultFolderRelativePath(task)
            )
        }
    }

    @Test
    fun getDefaultContentTest() {
        listOf(task1, task2, task3, task4, task5).forEach { task ->
            assertEquals(
                "Unexpected default content",
                task.loadContent(),
                DefaultContentProvider.getDefaultContent(task)
            )
        }
    }
}
