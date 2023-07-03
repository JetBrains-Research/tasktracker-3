package org.jetBrains.research.tasktracker.tracking.logger

import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.OutputStreamWriter

/**
 * Prints logs to the [logFile] in .csv format using [csvPrinter]
 */
data class LogPrinter(val csvPrinter: CSVPrinter, val fileWriter: OutputStreamWriter, val logFile: File) {
    companion object {
        private const val MAX_DIF_SIZE = 300
        private const val MAX_FILE_SIZE = 50 * 1024 * 1024
    }

    fun isFull() = logFile.length() > MAX_FILE_SIZE - MAX_DIF_SIZE
}
