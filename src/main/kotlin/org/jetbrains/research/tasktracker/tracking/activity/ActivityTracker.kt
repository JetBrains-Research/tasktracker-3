package org.jetbrains.research.tasktracker.tracking.activity

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import org.jetbrains.research.tasktracker.tracking.logger.ActivityLogger
import org.joda.time.DateTime

class ActivityTracker(project: Project) : Disposable {
    private val activityLogger: ActivityLogger

    init {
        activityLogger = ActivityLogger(project)
        Disposer.register(project, this) // TODO
    }

    // TODO: add config to select activities to track
    fun startTracking() {
        listenActions()
    }

    private fun listenActions() {
        val actionListener = object : AnActionListener {
            override fun beforeActionPerformed(anAction: AnAction, event: AnActionEvent) {
                val actionId = ActionManager.getInstance().getId(anAction) ?: return
                activityLogger.log(ActivityEvent(DateTime.now(), Type.Action, actionId))
            }
        }
        ApplicationManager.getApplication()
            .messageBus.connect()
            .subscribe(AnActionListener.TOPIC, actionListener)
    }

    @Suppress("EmptyFunctionBlock")
    override fun dispose() {
    }
}
