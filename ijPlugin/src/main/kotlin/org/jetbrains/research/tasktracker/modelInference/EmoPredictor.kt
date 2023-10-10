package org.jetbrains.research.tasktracker.modelInference

import com.intellij.openapi.diagnostic.Logger
import org.opencv.core.Mat

class EmoPrediction(val probabilities: Map<Int, Double>) {
    private val logger = Logger.getInstance(EmoPrediction::class.java)

    fun getPrediction(): Int {
        for (i in SENSITIVE_RANGE) {
            probabilities[i]?.let {
                if (it >= THRESHOLD) {
                    return i
                }
            } ?: logger.warn("There are no probability by key `$i`")
        }

        return probabilities.maxBy { it.value }.key
    }

    companion object {
        private const val THRESHOLD = 0.1
        private val SENSITIVE_RANGE: List<Int> = (7 downTo 2).toList()
    }
}

interface EmoPredictor {
    suspend fun predict(image: Mat): EmoPrediction
}
