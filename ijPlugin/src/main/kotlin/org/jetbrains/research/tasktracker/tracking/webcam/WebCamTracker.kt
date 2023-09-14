package org.jetbrains.research.tasktracker.tracking.webcam

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
import org.jetbrains.research.tasktracker.tracking.BaseTracker
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask

class WebCamTracker(project: Project) : BaseTracker {
    private val funtimer = Timer()

    @Suppress("MagicNumber")
    override fun startTracking() {
        // TODO: make it better
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

    // TODO: send emotion files
    override fun getLogFiles(): List<File> = emptyList()

    override fun stopTracking() { funtimer.cancel() }
}
