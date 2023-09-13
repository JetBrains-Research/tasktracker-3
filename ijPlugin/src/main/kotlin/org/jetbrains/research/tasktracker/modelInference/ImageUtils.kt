package org.jetbrains.research.tasktracker.modelInference

import org.bytedeco.opencv.global.opencv_cudawarping.resize
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.global.opencv_imgcodecs.imwrite
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Size

object ImageUtils {

    fun resizeImage() {
        // Load the input image
        // Load the input image
        val inputImage: Mat = imread("/Users/maria.tigina/IdeaProjects/emotional-monitoring/ijPlugin/src/main/resources/img/img.png")

        // Check if the image was loaded successfully

        // Check if the image was loaded successfully
        if (inputImage.empty()) {
            System.err.println("Could not load input image.")
            System.exit(1)
        }

        // Create an output Mat with the desired size (64x64)

        // Create an output Mat with the desired size (64x64)
        val outputImage = Mat(64, 64, inputImage.type())

        // Resize the input image to the desired size

        // Resize the input image to the desired size
        resize(inputImage, outputImage, Size(64, 64))

        // Save the resized image to a file

        // Save the resized image to a file
        imwrite("/Users/maria.tigina/IdeaProjects/emotional-monitoring/ijPlugin/src/main/resources/img/img_out.png", outputImage)

        // Release resources

        // Release resources
        inputImage.release()
        outputImage.release()
    }
}

fun main() {
    ImageUtils.resizeImage()
}