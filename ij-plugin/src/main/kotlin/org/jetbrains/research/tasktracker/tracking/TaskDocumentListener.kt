package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.tracking.logger.DocumentLogger

class TaskDocumentListener(val project: Project) : DocumentListener {
    private val logger: Logger = Logger.getInstance(javaClass)

    init {
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: init documents listener")
    }

    // Tracking documents changes before to be consistent with activity-tracker plugin
    override fun beforeDocumentChange(event: DocumentEvent) {
        DocumentLogger.log(project, event.document)
    }
}
