package org.jetbrains.research.tasktracker.tracking.webcam

import EmoClient
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
import org.jetbrains.research.tasktracker.actions.tracking.NotificationIcons
import org.jetbrains.research.tasktracker.actions.tracking.NotificationWrapper
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.modelInference.frameToMat
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.tracking.logger.WebCamLogger
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import org.joda.time.DateTime
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask

class WebCamTracker(project: Project) : BaseTracker {
    private val webcamLogger: WebCamLogger = WebCamLogger(project)

    private val timerToMakePhoto = Timer()
    private val emoPredictor: EmoPredictor = EmoClient()

    @Suppress("MagicNumber", "TooGenericExceptionCaught", "SwallowedException")
    override fun startTracking() {
        var photosMade = 0L
        // TODO: make it better
        timerToMakePhoto.scheduleAtFixedRate(
            timerTask {
                makePhoto()?.let {
                    photosMade++
                    runBlocking {
                        try {
                            val photoDate = DateTime.now()
                            val prediction = emoPredictor.predict(frameToMat(it))
                            val modelScore = prediction.getPrediction()
                            EmotionType.byModelScore(modelScore).also {
                                webcamLogger.log(it, prediction.probabilities, true, photoDate)
                                GlobalPluginStorage.currentEmotion = it
                            }
                        } catch (e: Exception) {
                            // do nothing
                        }
                    }
                    if (photosMade == PHOTOS_MADE_BEFORE_NOTIFICATION) {
                        // TODO: Put into a coroutine scope to call
                        showNotification()
                    }
                }
            },
            TIME_TO_PHOTO_DELAY,
            TIME_TO_PHOTO_DELAY
        )
    }

    private fun showNotification() {
        ApplicationManager.getApplication().invokeAndWait {
            ApplicationManager.getApplication().runReadAction {
                NotificationWrapper(
                    NotificationIcons.feedbackNotificationIcon,
                    NotificationWrapper.FEEDBACK_TEXT,
                    null
                ).show()
            }
        }
    }

    override fun getLogFiles(): List<File> = listOf(webcamLogger.logPrinter.logFile)

    override fun stopTracking() {
        timerToMakePhoto.cancel()
    }

    companion object {
        // 5 sec
        private const val TIME_TO_PHOTO_DELAY = 5000L

        private const val PHOTOS_MADE_BEFORE_NOTIFICATION = 60 * 60 / (TIME_TO_PHOTO_DELAY / 1000)
    }
}
