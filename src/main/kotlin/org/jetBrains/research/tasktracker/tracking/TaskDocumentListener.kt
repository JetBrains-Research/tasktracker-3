package org.jetBrains.research.tasktracker.tracking

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig

class TaskDocumentListener : DocumentListener {
    private val logger: Logger = Logger.getInstance(javaClass)

    init {
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: init documents listener")
    }

    // Tracking documents changes before to be consistent with activity-tracker plugin
    override fun beforeDocumentChange(event: DocumentEvent) {
        if (isValidChange(event)) {
            DocumentLogger.log(event.document)
        }
    }

    // To avoid completion events with IntellijIdeaRulezzz sign.
    // TODO: Maybe we should remove it since we don't use EditorFactory
    // to add listeners anymore (see https://intellij-support.jetbrains.com/hc/en-us/community/posts/115000664264-IntelliJ-DocumentListener-gives-an-internally-inconsistent-event-stream)
    private fun isValidChange(event: DocumentEvent): Boolean {
        return EditorFactory.getInstance().getEditors(event.document).isNotEmpty() && FileDocumentManager.getInstance()
            .getFile(event.document) != null
    }
}
