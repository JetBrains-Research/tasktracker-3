package org.jetBrains.research.tasktracker.tracking

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetBrains.research.tasktracker.models.Extension
import org.jetBrains.research.tasktracker.tracking.task.Task
import java.util.*

object DefaultContentProvider {
    private val lineSeparator = System.lineSeparator()
    fun getDefaultFolderRelativePath(task: Task) =
        "$PLUGIN_NAME/${task.getExtension().name.lowercase(Locale.getDefault())}"

    fun getDefaultContent(task: Task) = when (task.getExtension()) {
        Extension.JAVA ->
            """
                ${getPackage(task.getExtension())}
               
                public class Solution {
                
                    public static void main(String[] args) {
                    
                            // Write your code here
                            
                    }
                    
                }
            """.trimIndent()

        Extension.KOTLIN ->
            """
                ${getPackage(task.getExtension())}
                
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

    private fun getPackage(extension: Extension): String {
        val currentPackage =
            "package $PLUGIN_NAME.${extension.name.lowercase(Locale.getDefault())}"
        return when (extension) {
            Extension.JAVA -> "$currentPackage;$lineSeparator$lineSeparator"
            Extension.KOTLIN -> "$currentPackage$lineSeparator$lineSeparator"
            else -> ""
        }
    }
}
