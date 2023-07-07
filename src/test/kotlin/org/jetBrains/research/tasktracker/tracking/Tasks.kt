@file:Suppress("MatchingDeclarationName")

package org.jetBrains.research.tasktracker.tracking

import org.jetBrains.research.tasktracker.models.Extension
import org.jetBrains.research.tasktracker.tracking.task.Task

data class MockTask(
    val filename: String,
    val ext: Extension,
    val text: String? = null,
    val relativePath: String? = null
) : Task {
    override fun getContent(): String? {
        return text
    }

    override fun getRelativeFilePath(): String? {
        return relativePath
    }

    override fun getFileName(): String {
        return filename
    }

    override fun getExtension(): Extension {
        return ext
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MockTask

        if (filename != other.filename) return false
        if (ext != other.ext) return false
        if (text != other.text) {
            if (text == null || other.text == null) {
                return text == DefaultContentProvider.getDefaultContent(this) ||
                    other.text == DefaultContentProvider.getDefaultContent(other)
            }
        }
        return true
    }
}

val task1 = MockTask("task1", Extension.CPP, text = "int main(){return 0;}")
val task2 = MockTask("task2", Extension.KOTLIN, relativePath = "tasks")
val task3 = MockTask("task3", Extension.JAVA)
val task4 = MockTask("task4", Extension.PYTHON, relativePath = "tasks")
val task5 = MockTask("task5", Extension.JUPYTER)
