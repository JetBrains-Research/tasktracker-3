package org.jetbrains.research.tasktracker.config.emotion

import kotlinx.serialization.Serializable

@Serializable
open class Emotion(
    /**
     * Represents position in the model.
     * **ATTENTION** only positive positions will be used on prediction.
     * Other ones can be used for the default state.
     */
    val modelPosition: Int,
    val name: String,
    /**
     * When we try to predict an emotion, we need a threshold,
     * after crossing which we can say for sure that this is the desired emotion.
     * So emotion is first prediction with the result >= threshold.
     */
    val threshold: Double
)
