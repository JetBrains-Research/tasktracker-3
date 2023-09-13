
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.opencv_core.Mat
import org.jetbrains.research.tasktracker.modelInference.prepareImage

@Serializable
data class ImageData(val image: List<List<Double>>)

@Serializable
data class ProbabilitiesResponse(val data: Map<Int, Double>)

suspend fun ping() {
    val serverUrl = "http://localhost:5230/ping" // Replace with your server URL
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                },
            )
        }
    }

    val response: Map<Int, Double> = client.get(serverUrl) {
        contentType(ContentType.Application.Json)
    }.body()

    println(response)
}

suspend fun main() {
    val serverUrl = "http://localhost:5230/predict" // Replace with your server URL
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                },
            )
        }
    }

    val dir = "/Users/maria.tigina/IdeaProjects/emotional-monitoring/ijPlugin/src/main/resources/img/"
    val inputImage: Mat =
        opencv_imgcodecs.imread(dir + "img.png")

    val prepImage = prepareImage(inputImage)

    // Convert Mat to a list of lists (image data)
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

    // Send the image data to the server
    val response: Map<Int, Double> = client.post(serverUrl) {
        contentType(ContentType.Application.Json)
        setBody(imageData)
    }.body()

    // Handle the response (probabilities)
    println("Emotion Probabilities: $response")

    client.close()
}
