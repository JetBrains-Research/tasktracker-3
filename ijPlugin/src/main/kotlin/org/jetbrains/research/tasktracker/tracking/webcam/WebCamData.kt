package org.jetbrains.research.tasktracker.tracking.webcam

import org.jetbrains.research.tasktracker.actions.emoji.EmotionType
import org.joda.time.DateTime

data class WebCamData(
    val time: DateTime,
    val emotionShown: EmotionType,
    /**
     * false – when collected because an active window changed, true – when collected on the minute basis
     */
    val isRegular: Boolean,
    val scores: Map<Int, Double>
)
