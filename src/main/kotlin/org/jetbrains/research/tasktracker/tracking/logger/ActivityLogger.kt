package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.tracking.activity.ActivityEvent
import org.jetbrains.research.tasktracker.tracking.activity.Type
import org.joda.time.DateTime
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class ActivityLogger(val project: Project) {
    // TODO: to list of printers
    private val logPrinter: LogPrinter

    init {
        val logFile = createLogFile()
        val fileWriter = OutputStreamWriter(FileOutputStream(logFile), StandardCharsets.UTF_8)
        val csvPrinter = CSVPrinter(fileWriter, CSVFormat.DEFAULT)
        logPrinter = LogPrinter(csvPrinter, fileWriter, logFile)
        logPrinter.csvPrinter.printRecord(ActivityLoggedData.headers)
    }

    fun log(type: Type, info: String, id: Int? = null) {
        log(ActivityEvent(DateTime.now(), type, info, getSelectedText(), id?.toString()))
    }

    private fun log(activityEvent: ActivityEvent) {
        logPrinter.csvPrinter.printRecord(ActivityLoggedData.getData(activityEvent))
        logPrinter.csvPrinter.flush() // TODO remove flush here, add it on activity tracking stop
    }

    /**
     * @return selected text in the currently open file
     */
    private fun getSelectedText(): String? =
        ApplicationManager.getApplication().runReadAction<String?> {
            FileEditorManager.getInstance(project)
                .selectedTextEditor?.selectionModel?.selectedText
        }

    private fun createLogFile(): File {
        // TODO: write headers
        File(MainTaskTrackerConfig.pluginFolderPath).mkdirs()
        val logFileName =
            "activity_${project.hashCode()}_${project.name}.csv"
        val logFile = File("${MainTaskTrackerConfig.pluginFolderPath}/$logFileName")
        FileUtil.createIfDoesntExist(logFile)
        return logFile
    }
}
