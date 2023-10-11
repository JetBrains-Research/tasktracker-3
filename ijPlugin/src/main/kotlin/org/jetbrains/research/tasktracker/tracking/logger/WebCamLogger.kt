package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.emoji.Emotion
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamData
import org.joda.time.DateTime

class WebCamLogger(val project: Project) : BaseLogger() {
    override val logPrinterFilename: String = "emotions_${project.hashCode()}_${project.name}"
    override val loggedData: LoggedData<*, *>
        get() = WebcamLoggedData

    fun log(
        emotion: Emotion,
        scores: Map<Int, Double>,
        isRegular: Boolean,
        date: DateTime = DateTime.now()
    ) =
        log(WebcamLoggedData.getData(WebCamData(date, emotion, isRegular, scores)))
}
