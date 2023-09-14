package org.jetbrains.research.tasktracker.modelInference.model

import io.kinference.core.KIEngine
import io.kinference.core.data.tensor.KITensor
import io.kinference.core.data.tensor.asTensor
import io.kinference.core.model.KIModel
import io.kinference.ndarray.arrays.FloatNDArray
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.modelInference.EmoPrediction
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.modelInference.getPixel
import org.jetbrains.research.tasktracker.modelInference.prepareImage
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import kotlin.math.exp

class EmoModel : EmoPredictor {

    private fun softmax(input: FloatArray): FloatArray {
        val max = (input.maxOrNull() ?: 0.0) as Float
        val expSum = input.map { exp(it - max) }.sum()

        return input.map { exp(it - max) / expSum }.toFloatArray()
    }

    companion object {
        private const val MODEL_PATH = "emotion-ferplus-18.onnx"
        private val INPUT_SHAPE = intArrayOf(1, 1, 64, 64)
    }

    init {
        runBlocking {
            load()
        }
    }

    private lateinit var model: KIModel
    suspend fun load() {
//        model = KIEngine.loadModel(EmoModel::class.java.getResource(MODEL_PATH).readBytes())
        model = KIEngine.loadModel(
            EmoModel::class.java
            .getResource(MODEL_PATH)?.readBytes()?: error("$MODEL_PATH must exist")
        )
    }

    override suspend fun predict(image: Mat): EmoPrediction {
        val prepImage = prepareImage(image)
        val tensor = FloatNDArray(INPUT_SHAPE) { idx: IntArray ->
            getPixel(idx, prepImage)
        }

        val outputs = model.predict(listOf(tensor.asTensor("Input3")))
        val output = outputs["Plus692_Output_0"]
        val softmaxedOutput = ((output as KITensor).data as FloatNDArray).softmax()
        val outputArray = softmaxedOutput.array.toArray()

        val probabilities = outputArray.mapIndexed { index: Int, prob: Float -> index to prob.toDouble() }.toMap()
        println(probabilities)
        return EmoPrediction(probabilities)
    }
}

fun main() {
    val dir = "/Users/maria.tigina/IdeaProjects/emotional-monitoring/ijPlugin/src/main/resources/img/"
    val inputImage: Mat = Imgcodecs.imread(dir + "img.png")

    runBlocking {
        val model = EmoModel()
        val results = model.predict(inputImage)
        println(results)
    }
}
