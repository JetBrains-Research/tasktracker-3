package org.jetbrains.research.tasktracker.tracking.webcam

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.logger.WebCamLogger

class WebCamTracker(private val project: Project, private val emoPredictor: EmoPredictor) : BaseTracker("webCam") {
    override val trackerLogger: WebCamLogger = WebCamLogger(project)

    override fun startTracking() {
        project.getService(WebCamService::class.java).startTakingPhotos(emoPredictor, trackerLogger)
    }

    override fun stopTracking() {
        project.getService(WebCamService::class.java).stopTakingPhotos()
    }
}
