package org.jetbrains.research.tasktracker.util.logFile

@Suppress("UnusedPrivateMember")
enum class LogFileType(private val logFileType: String) {
    ActivityData("activity"),
    DocumentData("document"),
    FileEditorData("file-editor"),
    SurveyData("survey"),
    ToolWindowData("tool-window"),
    WebCamData("web-cam")
}
