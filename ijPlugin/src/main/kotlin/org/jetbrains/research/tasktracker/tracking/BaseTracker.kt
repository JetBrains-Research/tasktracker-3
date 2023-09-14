package org.jetbrains.research.tasktracker.tracking

import java.io.File

interface BaseTracker {
    fun startTracking()

    fun stopTracking()

    fun getLogFiles(): List<File>
}
