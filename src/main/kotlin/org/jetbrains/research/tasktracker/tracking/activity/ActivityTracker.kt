package org.jetbrains.research.tasktracker.tracking.activity

import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.process.BaseProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBusConnection
import org.jetbrains.research.tasktracker.tracking.logger.ActivityLogger
import java.awt.AWTEvent
import java.awt.event.KeyEvent

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
        listenKeyboard()
        listenExecution()
    }

    fun stopTracking() {
        messageBusConnections.forEach { it.disconnect() }
    }

    private fun listenActions(listenShortcut: Boolean = true) {
        val actionListener = object : AnActionListener {
            override fun beforeActionPerformed(anAction: AnAction, event: AnActionEvent) {
                val actionId = ActionManager.getInstance().getId(anAction) ?: return
                if (listenShortcut && !movingActions.contains(actionId) && !typingActions.contains(actionId)) {
                    val input = event.inputEvent
                    if (input is KeyEvent) {
                        activityLogger.log(Type.Shortcut, eventToShortcutInfo(input))
                    }
                }
                activityLogger.log(Type.Action, actionId)
            }
        }
        val connection = ApplicationManager.getApplication().messageBus.connect()
        messageBusConnections.add(connection)
        connection.subscribe(AnActionListener.TOPIC, actionListener)
    }

    private fun eventToShortcutInfo(event: KeyEvent): String {
        val keys = mutableListOf<String>()
        event.apply {
            if (isMetaDown) {
                keys.add("Meta")
            }
            if (isControlDown) {
                keys.add("Control")
            }
            if (isAltDown) {
                keys.add("Alt")
            }
            if (isShiftDown) {
                keys.add("Shift")
            }
        }
        keys.add(event.keyCode.toString())
        return keys.joinToString(" ")
    }

    private fun listenExecution() {
        val executionListener = object : ExecutionListener {
            override fun processStarting(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
                val commandLine = (handler as? BaseProcessHandler<*>)?.commandLine ?: ""
                activityLogger.log(Type.Execution, "$executorId:'${env.runProfile}':$commandLine")
            }
        }
        val connection = ApplicationManager.getApplication().messageBus.connect()
        messageBusConnections.add(connection)
        connection.subscribe(ExecutionManager.EXECUTION_TOPIC, executionListener)
    }

    private fun listenKeyboard() {
        IdeEventQueue.getInstance().addPostprocessor({ awtEvent: AWTEvent ->
            if (awtEvent is KeyEvent && awtEvent.id == KeyEvent.KEY_PRESSED) {
                activityLogger.log(Type.KeyPressed, "${awtEvent.id}:${awtEvent.keyChar}:${awtEvent.keyCode}")
            }
            if (awtEvent is KeyEvent && awtEvent.id == KeyEvent.KEY_RELEASED) {
                activityLogger.log(Type.KeyReleased, "${awtEvent.id}:${awtEvent.keyChar}:${awtEvent.keyCode}")
            }
            false
        }, Disposable { }) // TODO
    }

    companion object {
        private val movingActions = setOf(
            "EditorLeft", "EditorRight", "EditorDown", "EditorUp",
            "EditorLineStart", "EditorLineEnd", "EditorPageUp", "EditorPageDown",
            "EditorPreviousWord", "EditorNextWord",
            "EditorScrollUp", "EditorScrollDown",
            "EditorTextStart", "EditorTextEnd",
            "EditorDownWithSelection", "EditorUpWithSelection",
            "EditorRightWithSelection", "EditorLeftWithSelection",
            "EditorLineStartWithSelection", "EditorLineEndWithSelection",
            "EditorPageDownWithSelection", "EditorPageUpWithSelection"
        )

        private val typingActions = setOf(
            IdeActions.ACTION_EDITOR_BACKSPACE,
            IdeActions.ACTION_EDITOR_ENTER,
            IdeActions.ACTION_EDITOR_NEXT_TEMPLATE_VARIABLE
        )
    }
}
