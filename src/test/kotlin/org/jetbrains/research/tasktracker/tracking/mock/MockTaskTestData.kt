package org.jetbrains.research.tasktracker.tracking.mock

import org.jetbrains.research.tasktracker.models.Extension

val task1 = MockTask("task1", Extension.CPP, content = "int main(){return 0;}")
val task2 = MockTask("task2", Extension.KOTLIN, relativeFilePath = "tasks")
val task3 = MockTask("task3", Extension.JAVA)
val task4 = MockTask("task4", Extension.PYTHON, relativeFilePath = "tasks")
val task5 = MockTask("task5", Extension.JUPYTER)
