package org.jetbrains.research.tasktracker.tracking.webcam

import EmoClient
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.modelInference.frameToMat
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import java.util.*
import kotlin.concurrent.timerTask

class WebCamTracker(project: Project) {

    private val emoPredictor: EmoPredictor = EmoClient()

    @Suppress("MagicNumber")
    fun startTracking() {
        // TODO: make it better
        val funtimer = Timer()
        funtimer.scheduleAtFixedRate(
            timerTask {
                makePhoto()?.let {
                    runBlocking {
                        val prediction = emoPredictor.predict(frameToMat(it))
                        val modelScore = prediction.getPrediction()
                        GlobalPluginStorage.currentEmotion = EmotionType.byModelScore(modelScore)
                        println("A photo was made!!")
                    }
                }
            },
            5000,
            5000
        )
    }
}
