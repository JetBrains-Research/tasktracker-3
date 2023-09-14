package org.jetbrains.research.tasktracker.modelInference

import org.bytedeco.opencv.opencv_core.Mat

class EmoPrediction(private val probabilities: Map<Int, Double>) {
    fun getPrediction(): Int {
        return probabilities.maxBy { it.value }.key
    }
}

interface EmoPredictor {
    suspend fun predict(image: Mat): EmoPrediction
}
