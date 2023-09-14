package org.jetbrains.research.tasktracker.modelInference

import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

fun resizeImage(image: Mat, pixels: Double = 64.0): Mat {
    val resizedImage = Mat()
    Imgproc.resize(image, resizedImage, Size(pixels, pixels))

    return resizedImage
}

fun prepareImage(image: Mat): Mat {
    val gImage = grayImage(image)
    val resImage = resizeImage(gImage)

    return resImage
}

fun grayImage(image: Mat): Mat {
    val grayImage = Mat()
    Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_RGB2GRAY)

    return grayImage
}

fun getPixel(tensor: IntArray, image: Mat, i: Int = 2, j: Int = 3): Float {
    return image.get(tensor[i], tensor[j])[0].toFloat()
}
