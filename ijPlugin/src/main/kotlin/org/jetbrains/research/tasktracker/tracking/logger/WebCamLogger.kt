package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamData
import org.joda.time.DateTime

class WebCamLogger(val project: Project) : BaseLogger() {
    // TODO: to list of printers
    override val logPrinter: LogPrinter = initLogPrinter("emotions_${project.hashCode()}_${project.name}")
        .also {
            it.csvPrinter.printRecord(WebcamLoggedData.headers)
        }

    fun log(
        emotion: EmotionType,
        scores: Map<Int, Double>,
        isRegular: Boolean,
        date: DateTime = DateTime.now()
    ) =
        log(WebcamLoggedData.getData(WebCamData(date, emotion, isRegular, scores)))
}
