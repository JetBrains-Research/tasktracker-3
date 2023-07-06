package org.jetBrains.research.tasktracker.tracking

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetBrains.research.tasktracker.models.Extension
import org.jetBrains.research.tasktracker.tracking.task.Task

val task1 = object : Task {
    override fun getContent(): String {
        return "int main(){return 0;}"
    }

    override fun getRelativeFilePath(): String? {
        return null
    }

    override fun getFileName(): String {
        return "task1"
    }

    override fun getExtension(): Extension {
        return Extension.CPP
    }
}

val task2 = object : Task {
    override fun getContent(): String? {
        return null
    }

    override fun getRelativeFilePath(): String {
        return "tasks"
    }

    override fun getFileName(): String {
        return "task2"
    }

    override fun getExtension(): Extension {
        return Extension.KOTLIN
    }
}

val task3 = object : Task {
    override fun getContent(): String? {
        return null
    }

    override fun getRelativeFilePath(): String? {
        return null
    }

    override fun getFileName(): String {
        return "task3"
    }

    override fun getExtension(): Extension {
        return Extension.JAVA
    }
}

val task4 = object : Task {
    override fun getContent(): String? {
        return null
    }

    override fun getRelativeFilePath(): String {
        return "tasks"
    }

    override fun getFileName(): String {
        return "task4"
    }

    override fun getExtension(): Extension {
        return Extension.PYTHON
    }
}

val task5 = object : Task {
    override fun getContent(): String? {
        return null
    }

    override fun getRelativeFilePath(): String? {
        return null
    }

    override fun getFileName(): String {
        return "task5"
    }

    override fun getExtension(): Extension {
        return Extension.JUPYTER
    }
}

val CPP_CONTENT =
    """
                #include <iostream>
                
                int main() {
                
                    // Write your code here
                    
                    return 0;
                    
                }
    """.trimIndent()

const val PYTHON_CONTENT = "# Write your code here"

val KOTLIN_CONTENT =
    """
                package ${MainTaskTrackerConfig.PLUGIN_NAME}.tasks${System.lineSeparator()}${System.lineSeparator()}
                
                fun main() {
                
                    // Write your code here
                    
                }
    """.trimIndent()

val JAVA_CONTENT =
    """
                package ${MainTaskTrackerConfig.PLUGIN_NAME}.java;${System.lineSeparator()}${System.lineSeparator()}
               
                public class Solution {
                
                    public static void main(String[] args) {
                    
                            // Write your code here
                            
                    }
                    
                }
    """.trimIndent()
