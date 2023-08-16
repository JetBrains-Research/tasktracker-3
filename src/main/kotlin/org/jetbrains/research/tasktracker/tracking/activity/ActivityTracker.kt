package org.jetbrains.research.tasktracker.tracking.activity

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBusConnection
import org.jetbrains.research.tasktracker.tracking.logger.ActivityLogger

class ActivityTracker(project: Project) {
    private val activityLogger: ActivityLogger
    private val messageBusConnections: MutableList<MessageBusConnection>

    init {
        activityLogger = ActivityLogger(project)
        messageBusConnections = mutableListOf()
    }

    // TODO: add config to select activities to track
    fun startTracking() {
        listenActions()
    }

    fun stopTracking() {
        messageBusConnections.forEach { it.disconnect() }
    }

    private fun listenActions() {
        val actionListener = object : AnActionListener {
            override fun beforeActionPerformed(anAction: AnAction, event: AnActionEvent) {
                val actionId = ActionManager.getInstance().getId(anAction) ?: return
                activityLogger.logAction(actionId)
            }
        }
        val connection = ApplicationManager.getApplication().messageBus.connect()
        messageBusConnections.add(connection)
        connection.subscribe(AnActionListener.TOPIC, actionListener)
    }
}
