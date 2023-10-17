package org.jetbrains.research.tasktracker.requests

import com.intellij.openapi.diagnostic.Logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.tracking.activity.ActivityTracker
import org.jetbrains.research.tasktracker.tracking.fileEditor.FileEditorTracker
import org.jetbrains.research.tasktracker.tracking.toolWindow.ToolWindowTracker
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamTracker
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage
import java.io.File

object FileRequests {

    private val client = HttpClient(CIO)
    private val logger: Logger = Logger.getInstance(FileRequests::class.java)

    fun sendToolWindowFiles(toolWindowTracker: ToolWindowTracker) = toolWindowTracker.getLogFiles().all {
        sendFile(it, "toolWindow")
    }

    fun sendActivityFiles(activityTracker: ActivityTracker) = activityTracker.getLogFiles().all {
        sendFile(it, "activity")
    }

    fun sendFileEditorFiles(fileEditorTracker: FileEditorTracker) = fileEditorTracker.getLogFiles().all {
        sendFile(it, "fileEditor")
    }

    fun sendWebcamFiles(webCamTracker: WebCamTracker) = webCamTracker.getLogFiles().all {
        sendFile(it, "webCam")
    }

    fun sendSurvey(surveyFiles: List<File>) = surveyFiles.all { sendFile(it, "survey") }

    @Suppress("TooGenericExceptionCaught")
    private fun sendFile(file: File, subdir: String) = runBlocking {
        try {
            client.submitFormWithBinaryData(
                url = "$DOMAIN/upload-document/${MainPanelStorage.currentResearchId}?subdir=$subdir",
                formData = formData {
                    append(
                        "file",
                        file.readBytes(),
                        Headers.build {
                            append(
                                HttpHeaders.ContentDisposition, "filename=\"${file.name}\""
                            )
                        }
                    )
                }
            )
            true
        } catch (e: Exception) {
            logger.warn("Server interaction error! File to send: ${file.path}", e)
            false
        }
    }

    const val DOMAIN = "http://3.249.245.244:8888"
}
