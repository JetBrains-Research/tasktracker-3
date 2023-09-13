
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.opencv_core.Mat
import org.jetbrains.research.tasktracker.modelInference.EmoPrediction
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.modelInference.prepareImage

@Serializable
data class ImageData(val image: List<List<Double>>)


class EmoClient(private var serverUrl: String = "http://localhost:5230/predict") : EmoPredictor {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                },
            )
        }
    }

    override suspend fun predict(image: Mat): EmoPrediction {
        val prepImage = prepareImage(image)

        val imageList = mutableListOf<List<Double>>()
        for (row in 0 until prepImage.rows()) {
            val rowList = mutableListOf<Double>()
            for (col in 0 until prepImage.cols()) {
                val pixelValue = prepImage.ptr(row, col)[0].toDouble()
                rowList.add(pixelValue)
            }
            imageList.add(rowList)
        }

        val imageData = ImageData(imageList)

        val response: Map<Int, Double> = client.post(serverUrl) {
            contentType(ContentType.Application.Json)
            setBody(imageData)
        }.body()

        println("Emotion Probabilities: $response")

        return EmoPrediction(response)
    }
}

fun main() {
    val dir = "/Users/maria.tigina/IdeaProjects/emotional-monitoring/ijPlugin/src/main/resources/img/"
    val inputImage: Mat =
        opencv_imgcodecs.imread(dir + "img.png")

    runBlocking {
        val model = EmoClient()
        val results = model.predict(inputImage)
        println(results)
    }
}

