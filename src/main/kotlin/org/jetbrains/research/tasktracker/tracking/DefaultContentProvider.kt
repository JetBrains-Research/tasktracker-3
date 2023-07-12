package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetbrains.research.tasktracker.models.Extension
import org.jetbrains.research.tasktracker.tracking.task.Task
import java.util.*

object DefaultContentProvider {
    private val lineSeparator = System.lineSeparator()
    fun getDefaultFolderRelativePath(task: Task) =
        "$PLUGIN_NAME/${task.lowercaseExtension()}"

    fun getDefaultContent(task: Task) = when (task.extension) {
        Extension.JAVA ->
            """
                ${getPackage(task)}
               
                public class Solution {
                
                    public static void main(String[] args) {
                
                            // Write your code here
                
                    }
                
                }
            """.trimIndent()

        Extension.KOTLIN ->
            """
                ${getPackage(task)}
                
                fun main() {
                
                    // Write your code here
                
                }
            """.trimIndent()

        Extension.CPP ->
            """
                #include <iostream>
                
                int main() {
                
                    // Write your code here
                
                    return 0;
                
                }
            """.trimIndent()

        Extension.PYTHON -> "# Write your code here"
        else -> ""
    }

    private fun getPackage(task: Task): String {
        val currentPackage =
            "package $PLUGIN_NAME.${task.lowercaseExtension()}${task.relativePathToPackage()?.let { ".$it" } ?: ""}"
        return when (task.extension) {
            Extension.JAVA -> "$currentPackage;$lineSeparator$lineSeparator"
            Extension.KOTLIN -> "$currentPackage$lineSeparator$lineSeparator"
            else -> ""
        }
    }

    private fun Task.relativePathToPackage() = relativeFilePath
        ?.removePrefix("/")
        ?.removeSuffix("/")
        ?.replace("/", ".")

    private fun Task.lowercaseExtension() = extension.name.lowercase(Locale.getDefault())
}
