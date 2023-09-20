package org.jetbrains.research.tasktracker.tracking.fileEditor

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.messages.MessageBusConnection
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.logger.FileEditorLogger
import java.io.File

// TODO make photos and log Emotions if webcam switched on
class FileEditorTracker(
    private val project: Project,
) : BaseTracker {
    private var messageBusConnection: MessageBusConnection? = null
    private val logger = FileEditorLogger(project)

    override fun startTracking() {
        val fileEditorListener = object : FileEditorManagerListener {
            override fun selectionChanged(event: FileEditorManagerEvent) {
                super.selectionChanged(event)
                logger.log(FileEditorAction.FOCUS, event.oldFile?.name, event.newFile?.name)
            }

            override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
                super.fileOpened(source, file)
                logger.log(FileEditorAction.OPEN, null, file.name)
            }

            override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
                super.fileClosed(source, file)
                logger.log(FileEditorAction.CLOSE, file.name)
            }
        }
        messageBusConnection = project.messageBus.connect()
        messageBusConnection?.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, fileEditorListener)
    }

    override fun stopTracking() {
        messageBusConnection?.disconnect()
    }

    override fun getLogFiles(): List<File> = listOf(logger.logPrinter.logFile)
}
