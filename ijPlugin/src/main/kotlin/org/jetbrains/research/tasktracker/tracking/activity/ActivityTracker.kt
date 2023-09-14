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
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.util.messages.MessageBusConnection
import com.intellij.util.messages.Topic
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.activity.actions.MovingActions
import org.jetbrains.research.tasktracker.tracking.activity.actions.TypingActions
import org.jetbrains.research.tasktracker.tracking.logger.ActivityLogger
import java.awt.AWTEvent
import java.awt.event.KeyEvent
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class ActivityTracker(project: Project) : BaseTracker {
    val activityLogger: ActivityLogger = ActivityLogger(project)
    private val messageBusConnections: MutableList<MessageBusConnection> = mutableListOf()
    private var trackingDisposable: Disposable? = null
    private val currentId: AtomicInteger = AtomicInteger(1)

    override fun getLogFiles(): List<File> = listOf(activityLogger.logPrinter.logFile)

    // TODO: add config to select activities to track
    override fun startTracking() {
        trackingDisposable = Disposer.newDisposable()
        listenActions()
        listenKeyboard()
        listenExecution()
    }

    override fun stopTracking() {
        messageBusConnections.forEach { it.disconnect() }
        trackingDisposable?.let { Disposer.dispose(it) }
    }

    private fun listenActions(listenShortcut: Boolean = true) {
        val actionListener = object : AnActionListener {
            override fun beforeActionPerformed(anAction: AnAction, event: AnActionEvent) {
                val actionId = ActionManager.getInstance().getId(anAction)
                    ?: anAction.javaClass.name
                val id = currentId.getAndIncrement()
                if (listenShortcut) {
                    actionId.extractShortcut(event, id)
                }
                activityLogger.log(Type.Action, actionId, id)
            }
        }
        actionListener.connect(AnActionListener.TOPIC)
    }

    private fun String.extractShortcut(event: AnActionEvent, id: Int) {
        if (!movingActions.contains(this) && !typingActions.contains(this)) {
            val input = event.inputEvent
            if (input is KeyEvent) {
                activityLogger.log(Type.Shortcut, eventToShortcutInfo(input), id)
            }
        }
    }

    private fun eventToShortcutInfo(event: KeyEvent): String =
        Key.values().mapNotNull { it.check(event) }.map { it.name }
            .plus(event.keyCode.toString()).joinToString(" ")

    private fun listenExecution() {
        val executionListener = object : ExecutionListener {
            override fun processStarting(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
                val commandLine = (handler as? BaseProcessHandler<*>)?.commandLine ?: ""
                activityLogger.log(Type.Execution, "$executorId:'${env.runProfile}':$commandLine")
            }
        }
        executionListener.connect(ExecutionManager.EXECUTION_TOPIC)
    }

    private fun listenKeyboard() {
        IdeEventQueue.getInstance().addPostprocessor({ awtEvent: AWTEvent ->
            if (awtEvent is KeyEvent) {
                when (awtEvent.id) {
                    KeyEvent.KEY_PRESSED -> activityLogger.log(Type.KeyPressed, awtEvent.info())
                    KeyEvent.KEY_RELEASED -> activityLogger.log(Type.KeyReleased, awtEvent.info())
                }
            }
            false
        }, trackingDisposable)
    }

    private fun <L : Any> L.connect(topic: Topic<L>) {
        val connection = ApplicationManager.getApplication().messageBus.connect()
        messageBusConnections.add(connection)
        connection.subscribe(topic, this)
    }

    private fun KeyEvent.info() = "$id:$keyChar:$keyCode"

    companion object {
        private val movingActions = MovingActions.values().map { it.name }.toSet()
        private val typingActions = TypingActions.values().map { it.name }.toSet()
    }

    private enum class Key(val f: KeyEvent.() -> Boolean) {
        Meta(KeyEvent::isMetaDown),
        Control(KeyEvent::isControlDown),
        Alt(KeyEvent::isAltDown),
        Shift(KeyEvent::isShiftDown);

        fun check(event: KeyEvent): Key? = if (f(event)) this else null
    }
}
