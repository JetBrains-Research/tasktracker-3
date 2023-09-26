package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.util.io.FileUtil
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

abstract class BaseLogger {
    // TODO: to list of printers
    abstract val logPrinter: LogPrinter

    protected fun createLogFile(fileName: String): File {
        val logFileName = "$fileName.csv"
        val logFile = File("${MainTaskTrackerConfig.logFilesFolder}/$logFileName")
        FileUtil.createIfDoesntExist(logFile)
        return logFile
    }

    protected fun log(dataToPrint: Iterable<*>) {
        logPrinter.csvPrinter.printRecord(dataToPrint)
        logPrinter.csvPrinter.flush() // TODO remove flush here, add it on activity tracking stop
    }

    protected fun initLogPrinter(fileName: String): LogPrinter {
        val logFile = createLogFile(fileName)
        val fileWriter = OutputStreamWriter(FileOutputStream(logFile), StandardCharsets.UTF_8)
        val csvPrinter = CSVPrinter(fileWriter, CSVFormat.DEFAULT)
        return LogPrinter(csvPrinter, fileWriter, logFile)
    }
}
