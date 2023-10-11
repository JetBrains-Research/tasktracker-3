package org.jetbrains.research.tasktracker.ui.main.panel.storage

import org.jetbrains.research.tasktracker.config.emoji.DefaultEmotion
import org.jetbrains.research.tasktracker.config.emoji.Emotion
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamInfo

object GlobalPluginStorage {
    val camerasInfo: MutableList<WebCamInfo> = mutableListOf()
    var currentDeviceNumber: Int? = null
    var currentEmotion: Emotion = DefaultEmotion

    var emoPredictor: EmoPredictor? = null
}
