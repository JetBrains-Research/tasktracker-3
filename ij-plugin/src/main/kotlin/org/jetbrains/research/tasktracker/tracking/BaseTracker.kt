package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.tracking.logger.BaseLogger
import java.io.File

abstract class BaseTracker(override val logFileType: String = "") : Loggable() {

    protected abstract val trackerLogger: BaseLogger

    abstract fun startTracking()

    abstract fun stopTracking()

    override fun getLogFiles(): List<File> = trackerLogger.getLogFiles()
}
