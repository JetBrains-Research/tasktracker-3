package org.jetbrains.research.tasktracker.tracking.webcam

import com.intellij.openapi.project.Project
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
                    println("A photo was made!!")
                }
            },
            5000,
            5000
        )
    }
}
