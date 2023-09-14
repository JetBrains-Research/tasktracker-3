package org.jetbrains.research.tasktracker.modelInference

import org.opencv.core.Mat


class EmoPrediction(val probabilities: Map<Int, Double>) {
    fun getPrediction(): Int {
        for (i in (7 downTo 2)) {
            if (probabilities[i]!! >= 0.1) {
                return i
            }
        }

        return probabilities.maxBy { it.value }.key
    }
}

interface EmoPredictor {
    suspend fun predict(image: Mat): EmoPrediction
}
