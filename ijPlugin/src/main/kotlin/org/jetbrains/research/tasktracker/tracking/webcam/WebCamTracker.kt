package org.jetbrains.research.tasktracker.tracking.webcam

import EmoClient
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
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
        // TODO: make it better
        timerToMakePhoto.scheduleAtFixedRate(
            timerTask {
                makePhoto()?.let {
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
                }
            },
            5000,
            5000
        )
    }

    override fun getLogFiles(): List<File> = listOf(webcamLogger.logPrinter.logFile)

    override fun stopTracking() {
        timerToMakePhoto.cancel()
    }
}
