package org.jetbrains.research.tasktracker.requests

import com.intellij.openapi.diagnostic.Logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import org.apache.http.client.utils.URIBuilder
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.getRoute
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import java.io.File

object FileRequests {

    private val client = HttpClient(CIO)
    private val logger: Logger = Logger.getInstance(FileRequests::class.java)

    @Suppress("TooGenericExceptionCaught")
    suspend fun sendFile(file: File, logFileType: String): Boolean {
        try {
            val researchId = GlobalPluginStorage.currentResearchId
                ?: error("ResearchId is undefined")
            val url = URIBuilder(getRoute("upload-log-file")).addParameter("logFileType", logFileType)
                .addParameter("id", researchId.toString()).build().toString()
            client.submitFormWithBinaryData(
                url = url,
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
            return true
        } catch (e: IllegalStateException) {
            logger.warn(e.localizedMessage)
        } catch (e: Exception) {
            logger.warn("Server interaction error! File to send: ${file.path}", e)
        }
        return false
    }
}
