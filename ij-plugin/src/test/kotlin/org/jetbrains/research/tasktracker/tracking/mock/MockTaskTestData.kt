package org.jetbrains.research.tasktracker.tracking.mock

import org.jetbrains.research.tasktracker.models.Extension

val taskFile1 = MockTaskFile("task1", Extension.CPP, content = "int main(){return 0;}", relativePath = "task1")
val taskFile2Kotlin = MockTaskFile("task2", Extension.KOTLIN, relativePath = "task2")
val taskFile2Java = MockTaskFile("task2", Extension.JAVA, relativePath = "task2/java")
val taskFile3 = MockTaskFile("task3", Extension.PYTHON, relativePath = "task3")
val taskFile4 = MockTaskFile("task4", Extension.JUPYTER, relativePath = "task4/jupyter")

val task1 = MockTask("task1", listOf(taskFile1))
val task2 = MockTask("task2", listOf(taskFile2Java, taskFile2Kotlin), "tasks")
val task3 = MockTask("task3", listOf(taskFile3), "example")
val task4 = MockTask("task4", listOf(taskFile4))
