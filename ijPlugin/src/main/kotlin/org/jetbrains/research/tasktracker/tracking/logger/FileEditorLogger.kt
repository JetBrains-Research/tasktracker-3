package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.tracking.fileEditor.FileEditorAction
import org.jetbrains.research.tasktracker.tracking.fileEditor.FileEditorData
import org.joda.time.DateTime

class FileEditorLogger(project: Project) : BaseLogger() {
    override val logPrinterFilename: String = "file_editor_${project.hashCode()}_${project.name}"
    override val loggedData: LoggedData<*, *>
        get() = FileEditorLoggedData

    fun log(
        action: FileEditorAction,
        oldFileName: String? = null,
        newFileName: String? = null,
        date: DateTime = DateTime.now()
    ) = log(FileEditorLoggedData.getData(FileEditorData(date, action, oldFileName, newFileName)))
}
