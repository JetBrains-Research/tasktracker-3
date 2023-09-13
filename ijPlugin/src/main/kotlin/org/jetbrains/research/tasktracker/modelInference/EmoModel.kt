package org.jetbrains.research.tasktracker.modelInference

import io.kinference.core.KIEngine
import io.kinference.core.data.tensor.KITensor
import io.kinference.core.data.tensor.asTensor
import io.kinference.core.model.KIModel
import io.kinference.ndarray.arrays.FloatNDArray
import kotlinx.coroutines.runBlocking
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.opencv_core.Mat
import kotlin.math.exp

class EmoModel {

    class EmoModelResult(private val probabilities: DoubleArray) {
        fun getProbabilities(): Map<Int, Double> {
            return softmax(probabilities).mapIndexed { index: Int, prob: Double -> index to prob }.toMap()
        }

        fun getPrediction(): Int {
            return getProbabilities().maxBy { it.value }.key
        }

        companion object {
            private fun softmax(input: DoubleArray): DoubleArray {
                val max = input.maxOrNull() ?: 0.0
                val expSum = input.map { exp(it - max) }.sum()

                return input.map { exp(it - max) / expSum }.toDoubleArray()
            }
        }
    }

    companion object {
        private const val MODEL_PATH = "/model/emotion-ferplus-18.onnx"
        private val INPUT_SHAPE = intArrayOf(1, 1, 64, 64)
    }

    private lateinit var model: KIModel
    suspend fun load() {
        model = KIEngine.loadModel(EmoModel::class.java.getResource(MODEL_PATH).readBytes())
    }

    suspend fun infer(image: Mat): EmoModelResult {

        val prepImage = prepareImage(image)

        val tensor = FloatNDArray(INPUT_SHAPE) { it: IntArray ->
            prepImage.ptr(it[2], it[3]).float
        }.asTensor("Input3")

        val prediction = model.predict(listOf(tensor))["Plus692_Output_0"]!! as KITensor
        val probabilities = (prediction.data as FloatNDArray).array.blocks[0].map { it.toDouble() }.toDoubleArray()

        return EmoModelResult(probabilities)
    }
}

fun main() {
    val dir = "/Users/maria.tigina/IdeaProjects/emotional-monitoring/ijPlugin/src/main/resources/img/"
    val inputImage: Mat =
        opencv_imgcodecs.imread(dir + "img.png")

    runBlocking {
        val model = EmoModel()
        model.load()
        val results = model.infer(inputImage)
        println(results)
    }
}
