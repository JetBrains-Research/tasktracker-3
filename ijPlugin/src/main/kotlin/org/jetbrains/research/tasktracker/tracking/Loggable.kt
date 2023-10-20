package org.jetbrains.research.tasktracker.tracking

import java.io.File

interface Loggable {

    val subDir: String

    fun getLogFiles(): List<File>
}
