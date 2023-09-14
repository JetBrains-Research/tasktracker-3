package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.tracking.activity.ActivityEvent
import org.jetbrains.research.tasktracker.tracking.survey.SurveyData
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowData
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamData
import org.joda.time.DateTime

data class LoggedDataGetter<T, S>(val header: String, val getData: (T) -> S)

abstract class LoggedData<T, S> {
    protected abstract val loggedDataGetters: List<LoggedDataGetter<T, S>>

    val headers: List<String>
        get() = loggedDataGetters.map { it.header }

    fun getData(t: T): List<S> {
        return loggedDataGetters.map { it.getData(t) }
    }
}

object DocumentLoggedData : LoggedData<Document, String?>() {
    override val loggedDataGetters: List<LoggedDataGetter<Document, String?>> = arrayListOf(
        LoggedDataGetter("date") { DateTime.now().toString() },
        LoggedDataGetter("timestamp") { it.modificationStamp.toString() },
        LoggedDataGetter("fileName") { FileDocumentManager.getInstance().getFile(it)?.name },
        LoggedDataGetter("fileHashCode") { FileDocumentManager.getInstance().getFile(it)?.hashCode().toString() },
        LoggedDataGetter("documentHashCode") { it.hashCode().toString() },
        LoggedDataGetter("fragment") { it.text },
        LoggedDataGetter("testMode") { TaskTrackerPlugin.mainConfig.pluginProperties.testMode.propValue }
    )
}

object ActivityLoggedData : LoggedData<ActivityEvent, String?>() {
    override val loggedDataGetters: List<LoggedDataGetter<ActivityEvent, String?>> = arrayListOf(
        LoggedDataGetter("date") { it.time.toString() },
        LoggedDataGetter("type") { it.type.name },
        LoggedDataGetter("info") { it.info },
        LoggedDataGetter("selected-text") { it.selectedText },
        LoggedDataGetter("id") { it.id }
    )
}

object WebcamLoggedData : LoggedData<WebCamData, String?>() {
    override val loggedDataGetters: List<LoggedDataGetter<WebCamData, String?>> = arrayListOf(
        LoggedDataGetter("date") { it.time.toString() },
        LoggedDataGetter("emotion_shown") { it.emotionShown.name },
        LoggedDataGetter("is_regular") { it.isRegular.toString() },
        LoggedDataGetter("scores") { it.scores.toString() },
    )
}

object ToolWindowLoggedData : LoggedData<ToolWindowData, String?>() {
    override val loggedDataGetters: List<LoggedDataGetter<ToolWindowData, String?>> = arrayListOf(
        LoggedDataGetter("date") { it.time.toString() },
        LoggedDataGetter("action") { it.action.name },
        LoggedDataGetter("active_window") { it.activeWindow },
    )
}

object SurveyLoggedData : LoggedData<SurveyData, String?>() {
    override val loggedDataGetters: List<LoggedDataGetter<SurveyData, String?>> = arrayListOf(
        LoggedDataGetter("date") { it.time.toString() },
        LoggedDataGetter("question_id") { it.questionId.toString() },
        LoggedDataGetter("question") { it.question },
        LoggedDataGetter("option") { it.option ?: "" },
        LoggedDataGetter("answer") { it.answer },
    )
}
