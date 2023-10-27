package org.jetbrains.research.tasktracker.util

import org.apache.commons.csv.CSVFormat
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.Researches
import org.jetbrains.research.tasktracker.database.models.data.*
import java.io.File
import java.time.LocalDate

fun File.parseLogFile(logFileType: String, researchId: Int) {
    CSVFormat.Builder.create().apply {
        setIgnoreSurroundingSpaces(true)
    }.build().parse(inputStream().bufferedReader())
        .drop(1)
        .forEach {
            transaction {
                with(it.iterator()) {
                    when (logFileType) {
                        "activity" -> parseActivity(researchId)
                        "document" -> parseDocument(researchId)
                        "file-editor" -> parseFileEditor(researchId)
                        "survey" -> parseSurvey(researchId)
                        "tool-window" -> parseToolWindow(researchId)
                        "web-cam" -> parseWebCam(researchId)
                    }
                }
                commit()
            }
        }
}

fun Iterator<String>.parseActivity(researchId: Int) {
    ActivityData.insert {
        it[this.researchId] = EntityID(researchId, Researches)
        it[date] = LocalDate.parse(next())
        it[type] = next()
        it[info] = next()
        it[selectedText] = next()
        it[actionId] = next().toIntOrNull()
    }
}

fun Iterator<String>.parseDocument(researchId: Int) {
    DocumentData.insert {
        it[this.researchId] = EntityID(researchId, Researches)
        it[date] = LocalDate.parse(next())
        it[timestamp] = next().toLong()
        it[filename] = next()
        it[fileHashCode] = next().toInt()
        it[documentHashCode] = next().toInt()
        it[fragment] = next()
        it[testMode] = next()
    }
}

fun Iterator<String>.parseFileEditor(researchId: Int) {
    FileEditorData.insert {
        it[this.researchId] = EntityID(researchId, Researches)
        it[date] = LocalDate.parse(next())
        it[action] = next()
        it[oldFile] = next()
        it[newFile] = next()
    }
}

fun Iterator<String>.parseSurvey(researchId: Int) {
    SurveyData.insert {
        it[this.researchId] = EntityID(researchId, Researches)
        it[date] = LocalDate.parse(next())
        it[questionId] = next().toInt()
        it[question] = next()
        it[option] = next()
        it[answer] = next()
    }
}

fun Iterator<String>.parseToolWindow(researchId: Int) {
    ToolWindowData.insert {
        it[this.researchId] = EntityID(researchId, Researches)
        it[date] = LocalDate.parse(next())
        it[action] = next()
        it[activeWindow] = next()
    }
}

fun Iterator<String>.parseWebCam(researchId: Int) {
    WebCamData.insert {
        it[this.researchId] = EntityID(researchId, Researches)
        it[date] = LocalDate.parse(next())
        it[emotionShown] = next()
        it[isRegular] = next().toBoolean()
        it[scores] = next()
    }
}
