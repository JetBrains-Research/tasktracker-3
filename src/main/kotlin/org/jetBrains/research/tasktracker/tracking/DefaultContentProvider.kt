package org.jetBrains.research.tasktracker.tracking

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetBrains.research.tasktracker.models.Extension
import org.jetBrains.research.tasktracker.tracking.task.Task
import java.util.*

object DefaultContentProvider {
    private val lineSeparator = System.lineSeparator()
    fun getDefaultFolderRelativePath(task: Task) =
        "$PLUGIN_NAME/${lowercaseExtension(task)}"

    fun getDefaultContent(task: Task) = when (task.getExtension()) {
        Extension.JAVA -> """
                ${getPackage(task)}
               
                public class Solution {
                
                    public static void main(String[] args) {
                    
                            // Write your code here
                            
                    }
                    
                }
            """.trimIndent()

        Extension.KOTLIN -> """
                ${getPackage(task)}
                
                fun main() {
                
                    // Write your code here
                    
                }
            """.trimIndent()

        Extension.CPP -> """
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
        val currentPackage = "package $PLUGIN_NAME.${
            task.getRelativeFilePath()?.let { relativePathToPackage(it) } ?: lowercaseExtension(task)
        }"
        return when (task.getExtension()) {
            Extension.JAVA -> "$currentPackage;$lineSeparator$lineSeparator"
            Extension.KOTLIN -> "$currentPackage$lineSeparator$lineSeparator"
            else -> ""
        }
    }

    private fun relativePathToPackage(path: String): String {
        var pack = path.replace("/", ".")
        if (pack.first() == '/') {
            pack = pack.substring(1)
        }
        if (pack.last() == '/') {
            pack = pack.dropLast(1)
        }
        return pack
    }

    private fun lowercaseExtension(task: Task) = task.getExtension().name.lowercase(Locale.getDefault())
}
