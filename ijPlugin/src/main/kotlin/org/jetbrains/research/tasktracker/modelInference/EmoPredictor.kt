package org.jetbrains.research.tasktracker.modelInference

import org.opencv.core.Mat

class EmoPrediction(val probabilities: Map<Int, Double>) {

    companion object {
        private const val THRESHOLD = 0.1
        private val SENSITIVE_RANGE: List<Int> = (7 downTo 2).toList()
    }

    fun getPrediction(): Int {
        for (i in SENSITIVE_RANGE) {
            val probability = probabilities[i] ?: error("probability by index `$i` should exist")
            if (probability >= THRESHOLD) {
                return i
            }
        }

        return probabilities.maxBy { it.value }.key
    }
}

interface EmoPredictor {
    suspend fun predict(image: Mat): EmoPrediction
}
