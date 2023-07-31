package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.models.Extension

object DefaultContentProvider {
    private val lineSeparator = System.lineSeparator()

    fun getDefaultContent(extension: Extension, path: String) = when (extension) {
        Extension.JAVA ->
            """
                ${getPackage(extension, path)}
               
                public class Solution {
                
                    public static void main(String[] args) {
                
                            // Write your code here
                
                    }
                
                }
            """.trimIndent()

        Extension.KOTLIN ->
            """
                ${getPackage(extension, path)}
                
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

    private fun getPackage(extension: Extension, path: String): String {
        val currentPackage =
            "package ${path.relativePathToPackage()}"
        return when (extension) {
            Extension.JAVA -> "$currentPackage;$lineSeparator$lineSeparator"
            Extension.KOTLIN -> "$currentPackage$lineSeparator$lineSeparator"
            else -> ""
        }
    }

    private fun String.relativePathToPackage() = this
        .replace("//", "/")
        .removePrefix("/")
        .removeSuffix("/")
        .replace("/", ".")
}
