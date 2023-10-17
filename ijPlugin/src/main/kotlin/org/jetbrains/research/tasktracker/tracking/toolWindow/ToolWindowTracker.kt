package org.jetbrains.research.tasktracker.tracking.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.util.messages.MessageBusConnection
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.logger.ToolWindowLogger

// TODO make photos and log Emotions if webcam switched on
class ToolWindowTracker(
    private val project: Project,
) : BaseTracker() {
    override val trackerLogger: ToolWindowLogger = ToolWindowLogger(project)
    private var messageBusConnection: MessageBusConnection? = null

    override fun startTracking() {
        val toolWindowListener = object : ToolWindowManagerListener {
            override fun toolWindowShown(toolWindow: ToolWindow) {
                super.toolWindowShown(toolWindow)
                trackerLogger.log(toolWindow.id, ToolWindowAction.OPENED)
            }

            override fun stateChanged(
                toolWindowManager: ToolWindowManager,
                changeType: ToolWindowManagerListener.ToolWindowManagerEventType
            ) {
                super.stateChanged(toolWindowManager, changeType)
                if (changeType == ToolWindowManagerListener.ToolWindowManagerEventType.ActivateToolWindow) {
                    trackerLogger.log(toolWindowManager.activeToolWindowId ?: "", ToolWindowAction.FOCUSED)
                }
                // TODO find solution to track another eventTypes
            }
        }
        messageBusConnection = project.messageBus.connect()
        messageBusConnection?.subscribe(ToolWindowManagerListener.TOPIC, toolWindowListener)
    }

    override fun stopTracking() {
        messageBusConnection?.disconnect()
    }
}
