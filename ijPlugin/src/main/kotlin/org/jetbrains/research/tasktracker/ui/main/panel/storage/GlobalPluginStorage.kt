package org.jetbrains.research.tasktracker.ui.main.panel.storage

import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
import org.jetbrains.research.tasktracker.modelInference.EmoModel
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamInfo

object GlobalPluginStorage {
    val camerasInfo: MutableList<WebCamInfo> = mutableListOf()
    var currentDeviceNumber: Int? = null
    var currentEmotion = EmotionType.DEFAULT

    val emoPredictor: EmoPredictor = EmoModel()
}
