package org.jetbrains.research.tasktracker.requests

import com.intellij.openapi.diagnostic.Logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.apache.http.client.utils.URIBuilder
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.getRoute
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage
import java.io.File

object FileRequests {

    private val client = HttpClient(CIO)
    private val logger: Logger = Logger.getInstance(FileRequests::class.java)

    @Suppress("TooGenericExceptionCaught")
    fun sendFile(file: File, logFileType: String) = runBlocking {
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
            true
        } catch (e: IllegalStateException) {
            logger.error(e.localizedMessage)
            false
        } catch (e: Exception) {
            logger.error("Server interaction error! File to send: ${file.path}", e)
            false
        }
    }
}
