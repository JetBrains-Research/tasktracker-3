//package org.jetbrains.research.tasktracker.tracking.webcam
//
//import com.intellij.openapi.diagnostic.Logger
//import kotlinx.coroutines.runBlocking
////import nu.pattern.OpenCV
//import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
//import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
//import org.jetbrains.research.tasktracker.tracking.logger.WebCamLogger
//import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
//import org.joda.time.DateTime
////import org.opencv.core.Mat
////import org.opencv.imgcodecs.Imgcodecs
////import org.opencv.videoio.VideoCapture
//import java.io.File
//
//object WebCamUtils {
//    val logger: Logger = Logger.getInstance(WebCamUtils::class.java)
//}
//
//fun collectAllDevices(): List<WebCamInfo> {
////    OpenCV.loadShared()
//
//    getBaseTestSelfiePath().also { File(it).mkdirs() }
//    val devices = mutableListOf<WebCamInfo>()
//    var deviceNumber = 0
//    do {
//        val camera = VideoCapture(deviceNumber)
//
//        val frame = Mat()
//
//        if (!camera.isOpened || !makePhoto(camera, frame)) {
//            camera.release()
//            break
//        }
//
//        getTestSelfiePath(deviceNumber).also {
//            if (frame.saveSelfie(it)) {
//                devices.add(WebCamInfo(deviceNumber, it))
//            }
//        }
//        deviceNumber++
//        camera.release()
//    } while (!camera.isOpened || !makePhoto(camera, frame))
//    return devices
//}
//
//internal fun makePhoto() = GlobalPluginStorage.currentDeviceNumber?.let {
//    makePhoto(it)
//}
//
//internal fun makePhoto(deviceNumber: Int): Mat? {
//    val mat = Mat()
//
//    if (!makePhoto(deviceNumber, mat) || mat.empty()) {
//        return null
//    }
//
//    return mat
//}
//
//internal fun makePhoto(deviceNumber: Int, mat: Mat): Boolean {
//    val camera = VideoCapture(deviceNumber)
//    if (!camera.isOpened) {
//        camera.release()
//        return false
//    }
//
//    return makePhoto(camera, mat).also { camera.release() }
//}
//
//internal fun makePhoto(camera: VideoCapture, mat: Mat): Boolean {
//    repeat(ATTEMPTS_TO_TAKE_PHOTO) {
//        camera.read(mat)
//    }
//    return camera.grab()
//}
//
//fun Mat.saveSelfie(path: String): Boolean {
//    return Imgcodecs.imwrite(path, this)
//}
//
//private fun getBaseTestSelfiePath() = "${MainTaskTrackerConfig.pluginFolderPath}/test"
//
//private fun getTestSelfiePath(deviceNumber: Int) = "${getBaseTestSelfiePath()}/selfie_$deviceNumber.jpg"
//
//suspend fun Mat.guessEmotionAndLog(emoPredictor: EmoPredictor, webcamLogger: WebCamLogger, isRegular: Boolean = true) {
//    try {
//        val photoDate = DateTime.now()
//        val prediction = emoPredictor.predict(this)
//        val modelScore = prediction.getPrediction()
//        emoPredictor.emotionConfig.getEmotion(modelScore)?.let {
//            webcamLogger.log(it, prediction.probabilities, isRegular, photoDate)
//        } ?: error("can't find emotion with model position `$modelScore`")
//    } catch (e: IllegalStateException) {
//        WebCamUtils.logger.error(e)
//    }
//}
//
//fun makePhotoAndLogEmotion(emoPredictor: EmoPredictor, webcamLogger: WebCamLogger, isRegular: Boolean) {
//    makePhoto()?.let {
//        runBlocking {
//            it.guessEmotionAndLog(emoPredictor, webcamLogger, isRegular = isRegular)
//        }
//    }
//}
//
//const val ATTEMPTS_TO_TAKE_PHOTO = 10
