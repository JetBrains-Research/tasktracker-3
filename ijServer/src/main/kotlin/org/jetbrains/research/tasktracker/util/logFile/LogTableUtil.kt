package org.jetbrains.research.tasktracker.util.logFile

import io.ktor.util.logging.*
import org.apache.commons.csv.CSVFormat
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.data.*
import java.io.File

internal val LOGGER = KtorSimpleLogger("org.jetbrains.research.tasktracker.util.logFile")

fun File.parseLogFile(logFileTypeString: String, researchId: Int) {
    val logFileType = try {
        LogFileType.valueOf(logFileTypeString)
    } catch (exception: IllegalStateException) {
        LOGGER.warn("An data file was found that is not defined with the type: $logFileTypeString")
        return
    }
    CSVFormat.Builder.create().apply {
        setHeader()
        setSkipHeaderRecord(true)
    }.build().parse(inputStream().bufferedReader())
        .forEach {
            transaction {
                val table = when (logFileType) {
                    LogFileType.ActivityData -> ActivityData
                    LogFileType.DocumentData -> DocumentData
                    LogFileType.FileEditorData -> FileEditorData
                    LogFileType.SurveyData -> SurveyData
                    LogFileType.ToolWindowData -> ToolWindowData
                    LogFileType.WebCamData -> WebCamData
                }
                table.insertDefaultData(it.iterator(), researchId)
            }
        }
}
