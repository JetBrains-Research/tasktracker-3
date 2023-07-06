package org.jetBrains.research.tasktracker.tracking

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultContentProviderTest {

    private lateinit var defaultContentProvider: DefaultContentProvider

    @Before
    fun setUp() {
        defaultContentProvider = DefaultContentProvider
    }

    @Test
    fun getDefaultFolderRelativePathTest() {
        assertEquals("$PLUGIN_NAME/cpp", defaultContentProvider.getDefaultFolderRelativePath(task1))
        assertEquals("$PLUGIN_NAME/kotlin", defaultContentProvider.getDefaultFolderRelativePath(task2))
        assertEquals("$PLUGIN_NAME/java", defaultContentProvider.getDefaultFolderRelativePath(task3))
        assertEquals("$PLUGIN_NAME/python", defaultContentProvider.getDefaultFolderRelativePath(task4))
        assertEquals("$PLUGIN_NAME/jupyter", defaultContentProvider.getDefaultFolderRelativePath(task5))
    }

    @Test
    fun getDefaultContentTest() {
        assertEquals(CPP_CONTENT, defaultContentProvider.getDefaultContent(task1))
        assertEquals(KOTLIN_CONTENT, defaultContentProvider.getDefaultContent(task2))
        assertEquals(JAVA_CONTENT, defaultContentProvider.getDefaultContent(task3))
        assertEquals(PYTHON_CONTENT, defaultContentProvider.getDefaultContent(task4))
        assertEquals("", defaultContentProvider.getDefaultContent(task5))
    }
}
