package org.jetbrains.research.tasktracker.tracking.webcam

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.actions.tracking.NotificationIcons
import org.jetbrains.research.tasktracker.actions.tracking.NotificationWrapper
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.tracking.logger.WebCamLogger
import java.util.*
import kotlin.concurrent.timerTask

@Service(Service.Level.PROJECT)
class WebCamService : Disposable {

    private val timerToMakePhoto = Timer()
    private val logger = Logger.getInstance(WebCamService::class.java)

    override fun dispose() {
        logger.info("WebCamService has been disposed")
    }

    fun startTakingPhotos(emoPredictor: EmoPredictor, trackerLogger: WebCamLogger) {
        var photosMade = 0
        timerToMakePhoto.scheduleAtFixedRate(
            timerTask {
                makePhoto()?.let {
                    photosMade++
                    runBlocking {
                        it.guessEmotionAndLog(emoPredictor, trackerLogger)
                    }
                    if (photosMade == PHOTOS_MADE_BEFORE_NOTIFICATION) {
                        showNotification()
                    }
                }
            },
            TIME_TO_PHOTO_DELAY,
            TIME_TO_PHOTO_DELAY
        )
    }

    fun stopTakingPhotos() {
        timerToMakePhoto.cancel()
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

    companion object {
        // 5 sec
        private const val TIME_TO_PHOTO_DELAY = 5000L

        private const val PHOTOS_MADE_BEFORE_NOTIFICATION: Int = (60 * 60 / (TIME_TO_PHOTO_DELAY / 1000)).toInt()
    }
}
