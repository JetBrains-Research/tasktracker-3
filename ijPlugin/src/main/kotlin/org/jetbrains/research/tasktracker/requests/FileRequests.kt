package org.jetbrains.research.tasktracker.requests

import com.intellij.openapi.diagnostic.Logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.tracking.Loggable
import org.jetbrains.research.tasktracker.ui.main.panel.storage.MainPanelStorage
import java.io.File

object FileRequests {

    private val client = HttpClient(CIO)
    private val logger: Logger = Logger.getInstance(FileRequests::class.java)

    fun Loggable.send() = this.getLogFiles().all {
        sendFile(it, this.subDir)
    }

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
