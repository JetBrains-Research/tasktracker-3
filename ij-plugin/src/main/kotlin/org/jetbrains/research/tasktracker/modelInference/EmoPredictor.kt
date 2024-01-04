package org.jetbrains.research.tasktracker.modelInference

import org.jetbrains.research.tasktracker.config.emotion.EmotionConfig
//import org.opencv.core.Mat

class EmoPrediction(val probabilities: Map<Int, Double>, private val thresholds: Map<Int, Double>) {

    fun getPrediction(): Int {
        for (threshold in thresholds.entries) {
            val probability = probabilities[threshold.key] ?: error("probability by index `$threshold` should exist")
            if (probability >= threshold.value) {
                return threshold.key
            }
        }

        return probabilities.maxBy { it.value }.key
    }
}

interface EmoPredictor {

    val emotionConfig: EmotionConfig

//    suspend fun predict(image: Mat): EmoPrediction
}
