package org.jetbrains.research.tasktracker.modelInference

import io.kinference.core.KIEngine
import io.kinference.core.model.KIModel
import kotlinx.coroutines.runBlocking

class EmoModel {

    companion object {
        private const val MODEL_PATH = "/model/emotion-ferplus-18.onnx"
        private val INPUT_SHAPE = listOf(1, 1, 64, 64)
    }

    private val vectorizer = ImageVectorizer()
    private lateinit var model: KIModel
    suspend fun load() {
        model = KIEngine.loadModel(EmoModel::class.java.getResource(MODEL_PATH).readBytes())
    }

//    suspend fun infer(image: Image) {
//        val tensor = vectorizer.vectorize(INPUT_SHAPE, image)
//        model.predict(listOf(tensor))
//    }
}

fun main() {
    runBlocking {
        val model = EmoModel()
        model.load()
    }
}
