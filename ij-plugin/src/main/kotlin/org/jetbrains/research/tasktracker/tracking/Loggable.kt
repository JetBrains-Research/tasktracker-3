package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.requests.FileRequests
import java.io.File

abstract class Loggable {

    abstract val logFileType: String

    abstract fun getLogFiles(): List<File>

    suspend fun send() = getLogFiles().all {
        FileRequests.sendFile(it, this.logFileType)
    }
}
