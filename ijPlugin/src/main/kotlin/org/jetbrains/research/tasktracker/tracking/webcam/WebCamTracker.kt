package org.jetbrains.research.tasktracker.tracking.webcam

import EmoClient
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.modelInference.frameToMat
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask

class WebCamTracker(project: Project) : BaseTracker {
    private val funtimer = Timer()
    private val emoPredictor: EmoPredictor = EmoClient()

    @Suppress("MagicNumber")
    override fun startTracking() {
        // TODO: make it better
        funtimer.scheduleAtFixedRate(
            timerTask {
                runBlocking {
                    val prediction = emoPredictor.predict(frameToMat(it))
                    val modelScore = prediction.getPrediction()
                    GlobalPluginStorage.currentEmotion = EmotionType.byModelScore(modelScore)
                    println("A photo was made!!")
                }
            },
            5000,
            5000
        )
    }

    // TODO: send emotion files
    override fun getLogFiles(): List<File> = emptyList()

    override fun stopTracking() { funtimer.cancel() }
}
