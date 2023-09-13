package org.jetbrains.research.tasktracker.tracking.webcam

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import java.util.*
import kotlin.concurrent.timerTask

class WebCamTracker(project: Project) {
    @Suppress("MagicNumber")
    fun startTracking() {
        // TODO: make it better
        val funtimer = Timer()
        funtimer.scheduleAtFixedRate(
            timerTask {
                makePhoto()?.let {
                    GlobalPluginStorage.currentEmotion = EmotionType.values()
                        .filter { it != EmotionType.DEFAULT }.random()
                    println("A photo was made!!")
                }
            },
            5000,
            5000
        )
    }
}
