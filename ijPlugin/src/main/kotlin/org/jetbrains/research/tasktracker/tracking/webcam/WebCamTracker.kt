package org.jetbrains.research.tasktracker.tracking.webcam

import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.logger.WebCamLogger
import java.util.*
import kotlin.concurrent.timerTask

class WebCamTracker(project: Project, private val emoPredictor: EmoPredictor) : BaseTracker() {
    override val trackerLogger: WebCamLogger = WebCamLogger(project)

    private val timerToMakePhoto = Timer()

    override fun startTracking() {
        var photosMade = 0
        // TODO: make it better
        timerToMakePhoto.scheduleAtFixedRate(
            timerTask {
                makePhoto()?.let {
                    photosMade++
                    runBlocking {
                        it.guessEmotionAndLog(emoPredictor, trackerLogger)
                    }
                }
            },
            TIME_TO_PHOTO_DELAY,
            TIME_TO_PHOTO_DELAY
        )
    }

    override fun stopTracking() {
        timerToMakePhoto.cancel()
    }

    companion object {
        // 5 sec
        private const val TIME_TO_PHOTO_DELAY = 5000L
    }
}
