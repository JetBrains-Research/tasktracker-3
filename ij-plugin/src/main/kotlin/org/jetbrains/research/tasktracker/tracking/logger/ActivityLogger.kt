package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.tracking.activity.ActivityEvent
import org.jetbrains.research.tasktracker.tracking.activity.Type
import org.joda.time.DateTime

class ActivityLogger(val project: Project) : BaseLogger() {
    override val logPrinterFilename: String
        get() = "activity_${project.hashCode()}_${project.name}"
    override val loggedData: LoggedData<*, *>
        get() = ActivityLoggedData

    fun log(type: Type, info: String, id: Int? = null) =
        ActivityEvent(DateTime.now(), type, info, getSelectedText(), id?.toString()).also {
            log(ActivityLoggedData.getData(it))
        }

    /**
     * @return selected text in the currently open file
     */
    private fun getSelectedText(): String? = ApplicationManager.getApplication().runReadAction<String?> {
        FileEditorManager.getInstance(project).selectedTextEditor?.selectionModel?.selectedText
    }
}
