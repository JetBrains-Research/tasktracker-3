
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.research.tasktracker.modelInference.EmoPrediction
import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.modelInference.prepareImage
import org.opencv.core.Mat

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
                val pixelValue = prepImage.get(row, col)[0]
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
