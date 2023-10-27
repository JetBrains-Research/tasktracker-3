package org.jetbrains.research.tasktracker.util

import org.apache.commons.csv.CSVFormat
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.data.*
import java.io.File

fun File.parseLogFile(logFileType: String, researchId: Int) {
    CSVFormat.Builder.create().apply {
        setHeader()
        setSkipHeaderRecord(true)
    }.build().parse(inputStream().bufferedReader())
        .forEach {
            transaction {
                val table = when (logFileType) {
                    "activity" -> ActivityData
                    "document" -> DocumentData
                    "file-editor" -> FileEditorData
                    "survey" -> SurveyData
                    "tool-window" -> ToolWindowData
                    "web-cam" -> WebCamData
                    else -> null
                }
                table?.insertDefaultData(it.iterator(), researchId)
            }
        }
}
