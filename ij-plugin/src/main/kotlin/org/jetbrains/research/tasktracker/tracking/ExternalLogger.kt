package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.config.content.Log
import java.io.File
import java.nio.file.Path

class ExternalLogger(private val root: Path, private val log: Log) : Loggable() {
    override val logFileType: String = log.type

    override fun getLogFiles(): List<File> = log.logPaths.map {
        root.resolve(it).toFile()
    }.filter { it.exists() }
}
