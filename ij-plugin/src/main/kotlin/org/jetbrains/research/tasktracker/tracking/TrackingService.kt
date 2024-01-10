package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.tracking.activity.ActivityTracker
import org.jetbrains.research.tasktracker.tracking.fileEditor.FileEditorTracker
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowTracker

@Service(Service.Level.PROJECT)
class TrackingService : Disposable {

    private val trackers: MutableList<BaseTracker> = mutableListOf()

    fun startTracking(project: Project) {
        if (trackers.isNotEmpty()) { // Otherwise we can lose data
            return
        }
        trackers.addAll(
            listOf(
                ActivityTracker(project),
                ToolWindowTracker(project),
                FileEditorTracker(project)
            )
        )
        trackers.forEach { it.startTracking() }
    }

    /**
     * Stops the tracking process by calling the stopTracking method of each tracker.
     * After stopping the tracking, it invokes the send method of each tracker to send the tracked data.
     *
     * @param success A lambda that will be executed after the tracking is stopped and the data is sent successfully.
     * @param failure A lambda that will be executed if there is a failure in stopping the tracking or sending the data.
     */
    fun stopTracking(success: () -> Unit = {}, failure: () -> Unit = {}) {
        trackers.forEach {
            it.stopTracking()
        }
        ApplicationManager.getApplication().invokeAndWait {
            runBlocking {
                println(trackers.size)
                val result = trackers.all {
                    it.send()
                }
                trackers.clear()
                if (result) success.invoke() else failure.invoke()
            }
        }
    }

    override fun dispose() {
        stopTracking()
    }
}
