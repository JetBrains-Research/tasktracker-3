package org.jetbrains.research.tasktracker.tracking.webcam

import nu.pattern.OpenCV
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.videoio.VideoCapture
import java.io.File

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun collectAllDevices(): List<WebCamInfo> {
    OpenCV.loadShared()

    getBaseTestSelfiePath().also { File(it).mkdirs() }
    val devices = mutableListOf<WebCamInfo>()

    for (deviceNumber in 0..10) {
        val camera = VideoCapture(deviceNumber)

        if (!camera.isOpened) {
            camera.release()
            continue
        }

        val frame = Mat()
        if (!makePhoto(camera, frame)) {
            camera.release()
            continue
        }

        getTestSelfiePath(deviceNumber).also {
            if (frame.saveSelfie(it)) {
                devices.add(WebCamInfo(deviceNumber, it))
            }
        }

        camera.release()
    }

    return devices
}

internal fun makePhoto() = GlobalPluginStorage.currentDeviceNumber?.let {
    makePhoto(it)
}

internal fun makePhoto(deviceNumber: Int): Mat? {
    val mat = Mat()

    if (!makePhoto(deviceNumber, mat) || mat.empty()) {
        return null
    }

    return mat
}

internal fun makePhoto(deviceNumber: Int, mat: Mat): Boolean {
    val camera = VideoCapture(deviceNumber)
    if (!camera.isOpened) {
        camera.release()
        return false
    }

    return makePhoto(camera, mat).also { camera.release() }
}

internal fun makePhoto(camera: VideoCapture, mat: Mat): Boolean {
    for (i in 0..10) {
        camera.read(mat)
    }
    return camera.grab()
}

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun Mat.saveSelfie(path: String): Boolean {
    return Imgcodecs.imwrite(path, this)
}

private fun getBaseTestSelfiePath() = "${MainTaskTrackerConfig.pluginFolderPath}/test"

private fun getTestSelfiePath(deviceNumber: Int) = "${getBaseTestSelfiePath()}/selfie_$deviceNumber.jpg"
