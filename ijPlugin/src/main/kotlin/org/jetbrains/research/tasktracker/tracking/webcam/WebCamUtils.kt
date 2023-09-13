package org.jetbrains.research.tasktracker.tracking.webcam

import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.FrameGrabber
import org.bytedeco.javacv.OpenCVFrameConverter
import org.bytedeco.javacv.OpenCVFrameGrabber
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage
import java.io.File

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun collectAllDevices(): List<WebCamInfo> {
    getBaseTestSelfiePath().also { File(it).mkdirs() }
    var deviceNumber = 0
    val devices = mutableListOf<WebCamInfo>()
    do {
        val grabber: FrameGrabber = OpenCVFrameGrabber(deviceNumber)
        try {
            grabber.start()
            val frame = grabber.grab()
            grabber.release()

            getTestSelfiePath(deviceNumber).also {
                if (frame.saveSelfie(it)) {
                    devices.add(WebCamInfo(deviceNumber, it))
                }
            }
            deviceNumber++
        } catch (e: Exception) {
            deviceNumber = -1
        }
    } while (deviceNumber != -1)
    return devices
}

internal fun makePhoto() = MainPanelStorage.currentDeviceNumber?.let {
    val grabber = OpenCVFrameGrabber(it)
    grabber.start()
    val frame = grabber.grab()
    grabber.release()
    frame
}

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun Frame.saveSelfie(path: String): Boolean {
    return try {
        val converter = OpenCVFrameConverter.ToIplImage()
        val img = converter.convert(this)
        opencv_imgcodecs.cvSaveImage(path, img)
        true
    } catch (e: Exception) {
        false
    }
}

private fun getBaseTestSelfiePath() = "${MainTaskTrackerConfig.pluginFolderPath}/test"

private fun getTestSelfiePath(deviceNumber: Int) = "${getBaseTestSelfiePath()}/selfie_$deviceNumber.jpg"
