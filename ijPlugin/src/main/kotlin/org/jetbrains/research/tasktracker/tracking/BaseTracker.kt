package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.tracking.logger.BaseLogger
import java.io.File

abstract class BaseTracker {

    protected abstract val trackerLogger: BaseLogger

    abstract fun startTracking()

    abstract fun stopTracking()

    fun getLogFiles(): List<File> = trackerLogger.getLogFiles()
}
