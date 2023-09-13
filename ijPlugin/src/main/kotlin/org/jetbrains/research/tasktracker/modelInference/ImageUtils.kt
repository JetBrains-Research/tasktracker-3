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

fun resizeImage(image: Mat): Mat {
    val outputImage = Mat(64, 64, image.type())
    resize(image, outputImage, Size(64, 64))

    return outputImage
}

fun normalizeImage(image: Mat): Mat {
    val outputImage = Mat(64, 64, image.type())
    image.convertTo(outputImage, opencv_core.CV_8U);

    return outputImage
}

fun prepareImage(image: Mat): Mat {
    val gImage = grayImage(image)
    val resImage = resizeImage(gImage)
    val normImage = normalizeImage(resImage)

    return normImage
}

fun grayImage(image: Mat): Mat {
    val grayscaleMat = Mat()
    cvtColor(image, grayscaleMat, opencv_imgproc.CV_BGR2GRAY)

    return grayscaleMat
}
