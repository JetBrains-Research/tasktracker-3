package org.jetbrains.research.tasktracker.modelInference

import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.OpenCVFrameConverter.ToMat
import org.bytedeco.opencv.global.opencv_core
import org.bytedeco.opencv.global.opencv_imgproc
import org.bytedeco.opencv.global.opencv_imgproc.cvtColor
import org.bytedeco.opencv.global.opencv_imgproc.resize
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Size

fun frameToMat(frame: Frame): Mat {
    return ToMat().convert(frame)
}

fun resizeImage(image: Mat, pixels: Int = 64): Mat {
    val outputImage = Mat(pixels, pixels, image.type())
    resize(image, outputImage, Size(pixels, pixels))

    return outputImage
}

fun normalizeImage(image: Mat): Mat {
    val outputImage = Mat(image.cols(), image.rows(), image.type())
    image.convertTo(outputImage, opencv_core.CV_8U)

    return outputImage
}

fun prepareImage(image: Mat): Mat {
    val gImage = grayImage(image)
    val resImage = resizeImage(gImage)

    return normalizeImage(resImage)
}

fun grayImage(image: Mat): Mat {
    val grayscaleMat = Mat()
    cvtColor(image, grayscaleMat, opencv_imgproc.CV_BGR2GRAY)

    return grayscaleMat
}

fun getPixel(tensor: IntArray, image: Mat, i: Int = 2, j: Int = 3): Float {
    return image.ptr(tensor[i], tensor[j]).float
}
