package org.jetbrains.research.tasktracker.tracking.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.util.messages.MessageBusConnection
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.logger.ToolWindowLogger
import org.jetbrains.research.tasktracker.tracking.logger.WebCamLogger
import org.jetbrains.research.tasktracker.tracking.webcam.makePhotoAndLogEmotion
import java.io.File

class ToolWindowTracker(
    private val project: Project,
    private val emoPredictor: EmoPredictor,
    private val webcamLogger: WebCamLogger
) : BaseTracker {
    private val toolWindowLogger: ToolWindowLogger = ToolWindowLogger(project)
    private var messageBusConnection: MessageBusConnection? = null

    override fun startTracking() {
        val toolWindowListener = object : ToolWindowManagerListener {
            override fun toolWindowShown(toolWindow: ToolWindow) {
                super.toolWindowShown(toolWindow)
                makePhotoAndLogEmotion(emoPredictor, webcamLogger, false)
                toolWindowLogger.log(toolWindow.id, ToolWindowAction.OPENED)
            }

            override fun stateChanged(
                toolWindowManager: ToolWindowManager,
                changeType: ToolWindowManagerListener.ToolWindowManagerEventType
            ) {
                super.stateChanged(toolWindowManager, changeType)
                if (changeType == ToolWindowManagerListener.ToolWindowManagerEventType.ActivateToolWindow) {
                    makePhotoAndLogEmotion(emoPredictor, webcamLogger, false)
                    toolWindowLogger.log(toolWindowManager.activeToolWindowId ?: "", ToolWindowAction.FOCUSED)
                }
            }
        }
        messageBusConnection = project.messageBus.connect()
        messageBusConnection?.subscribe(ToolWindowManagerListener.TOPIC, toolWindowListener)
    }

    override fun stopTracking() {
        messageBusConnection?.disconnect()
    }

    override fun getLogFiles(): List<File> = listOf(toolWindowLogger.logPrinter.logFile)
}