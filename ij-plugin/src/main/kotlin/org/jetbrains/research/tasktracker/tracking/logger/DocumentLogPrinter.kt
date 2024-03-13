package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

/**
 * Takes care of all [logPrinters] (there may be several in case of log file overflowing), log a tracked document
 */
class DocumentLogPrinter {
    private var logPrinters = mutableListOf<LogPrinter>()

    companion object {
        private val logger: Logger = Logger.getInstance(this::class.java)
    }

    /**
     * Gets the active logPrinter or creates a new one if there was none or the active one was full
     */
    fun getActiveLogPrinter(project: Project, document: Document): LogPrinter {
        val activePrinter = getLastPrinter(project, document)
        return if (activePrinter.isFull()) {
            addLogPrinter(project, document)
        } else {
            activePrinter
        }
    }

    private fun getLastPrinter(project: Project, document: Document) = if (logPrinters.isEmpty()) {
        addLogPrinter(project, document)
    } else {
        logPrinters.last()
    }

    private fun addLogPrinter(project: Project, document: Document): LogPrinter {
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: init printer")
        val logFile = createLogFile(document)
        val fileWriter = OutputStreamWriter(FileOutputStream(logFile), StandardCharsets.UTF_8)
        val csvPrinter = CSVPrinter(fileWriter, CSVFormat.DEFAULT)
        csvPrinter.printRecord(DocumentLoggedData(project).headers)
        logPrinters.add(LogPrinter(csvPrinter, fileWriter, logFile))
        return logPrinters.last()
    }

    private fun createLogFile(document: Document): File {
        File(MainTaskTrackerConfig.logFilesFolder).mkdirs()
        val trackedFile = FileDocumentManager.getInstance().getFile(document)
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: create log file for tracked file ${trackedFile?.name}")
        val logFilesNumber = logPrinters.size
        val logFileName =
            "${trackedFile?.nameWithoutExtension}_${trackedFile.hashCode()}_${document.hashCode()}_$logFilesNumber.csv"
        val logFile = File("${MainTaskTrackerConfig.logFilesFolder}/$logFileName")
        FileUtil.createIfDoesntExist(logFile)
        return logFile
    }

    /**
     * Keep only the last active printer
     */
    fun removeInactivePrinters() {
        if (logPrinters.size > 1) {
            logPrinters.dropLast(1).forEach { it.csvPrinter.close() }
            logPrinters = mutableListOf(logPrinters.last())
        }
    }

    /**
     * We need to flush printers before getting their log files.
     */
    fun getLogFiles() = logPrinters.map {
        it.csvPrinter.flush()
        it.logFile
    }
}
