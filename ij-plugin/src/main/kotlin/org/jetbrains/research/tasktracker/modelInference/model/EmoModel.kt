//package org.jetbrains.research.tasktracker.modelInference.model
//
//import io.kinference.core.KIEngine
//import io.kinference.core.data.tensor.KITensor
//import io.kinference.core.data.tensor.asTensor
//import io.kinference.core.model.KIModel
//import io.kinference.ndarray.arrays.FloatNDArray
//import kotlinx.coroutines.runBlocking
//import org.jetbrains.research.tasktracker.config.emotion.EmotionConfig
//import org.jetbrains.research.tasktracker.modelInference.EmoPrediction
//import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
//import org.jetbrains.research.tasktracker.modelInference.getPixel
//import org.jetbrains.research.tasktracker.modelInference.prepare
//import org.opencv.core.Mat
//
//class EmoModel(override val emotionConfig: EmotionConfig) : EmoPredictor {
//
//    init {
//        runBlocking {
//            loadModel()
//        }
//    }
//
//    private lateinit var model: KIModel
//    private suspend fun loadModel() {
//        model = KIEngine.loadModel(
//            EmoModel::class.java
//                .getResource(emotionConfig.modelFilename)?.readBytes()
//                ?: error("${emotionConfig.modelFilename} must exist")
//        )
//    }
//
//    override suspend fun predict(image: Mat): EmoPrediction {
//        val prepImage = image.prepare()
//        val tensor = FloatNDArray(INPUT_SHAPE) { idx: IntArray ->
//            getPixel(idx, prepImage)
//        }
//
//        val outputs = model.predict(listOf(tensor.asTensor(emotionConfig.modelInputGate)))
//        val output = outputs[emotionConfig.modelOutputGate]
//        val softmaxOutput = ((output as KITensor).data as FloatNDArray).softmax()
//        val outputArray = softmaxOutput.array.toArray()
//
//        val probabilities = outputArray.mapIndexed { index: Int, prob: Float -> index to prob.toDouble() }.toMap()
//        return EmoPrediction(probabilities, emotionConfig.modelPositionToThreshold)
//    }
//
//    // TODO maybe we need to find a better solution for face detection?
//    companion object {
//        private val INPUT_SHAPE = intArrayOf(1, 1, 64, 64)
//    }
//}
