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

class EmoModel : EmoPredictor {

    private fun softmax(input: FloatArray): FloatArray {
        val max = (input.maxOrNull() ?: 0.0) as Float
        val expSum = input.map { exp(it - max) }.sum()

        return input.map { exp(it - max) / expSum }.toFloatArray()
    }

    companion object {
        private const val MODEL_PATH = "/model/emotion-ferplus-18.onnx"
        private val INPUT_SHAPE = intArrayOf(1, 1, 64, 64)
    }

    private lateinit var model: KIModel
    suspend fun load() {
        model = KIEngine.loadModel(EmoModel::class.java.getResource(MODEL_PATH).readBytes())
    }

    override suspend fun predict(image: Mat): EmoPrediction {
        val prepImage = prepareImage(image)

        val tensor = FloatNDArray(INPUT_SHAPE) {
            getPixel(it as IntArray, prepImage)
        }.asTensor("Input3")

        val prediction = model.predict(listOf(tensor))["Plus692_Output_0"]!! as KITensor
        val probabilities = softmax((prediction.data as FloatNDArray).array.blocks[0])
            .mapIndexed { index: Int, prob: Float -> index to prob.toDouble() }.toMap()

        return EmoPrediction(probabilities)
    }
}

fun main() {
    val dir = "/Users/maria.tigina/IdeaProjects/emotional-monitoring/ijPlugin/src/main/resources/img/"
    val inputImage: Mat =
        opencv_imgcodecs.imread(dir + "img.png")

    runBlocking {
        val model = EmoModel()
        model.load()
        val results = model.predict(inputImage)
        println(results)
    }
}
