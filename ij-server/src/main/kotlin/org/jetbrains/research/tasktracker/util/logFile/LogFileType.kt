package org.jetbrains.research.tasktracker.util.logFile

@Suppress("UnusedPrivateMember")
enum class LogFileType(val logFileType: String) {
    ActivityData("activity"),
    DocumentData("document"),
    FileEditorData("fileEditor"),
    SurveyData("survey"),
    ToolWindowData("toolWindow"),
    WebCamData("web-cam")
}
