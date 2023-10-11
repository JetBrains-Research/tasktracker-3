package org.jetbrains.research.tasktracker.config.emotion

import kotlinx.serialization.Serializable

@Serializable
open class Emotion(
    val modelPosition: Int,
    val name: String,
)
