package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.application.PathManager
import org.jetbrains.research.tasktracker.config.content.Log
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

class ExternalLogger(private val projectRoot: Path, private val log: Log) : Loggable() {
    override val logFileType: String = log.type

    override fun getLogFiles(): List<File> = log.logPaths.map {
        val root: Path = if (log.isInPluginDirectory) {
            Path(PathManager.getPluginsPath())
        } else {
            projectRoot
        }
        root.resolve(it).toFile()
    }.filter { it.exists() }
}
