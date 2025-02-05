package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.research.tasktracker.util.createLogFile
import org.jetbrains.research.tasktracker.util.logFile.parseLogFile

private const val DEFAULT_FOLDER = "default"

@Suppress("TooGenericExceptionCaught")
fun Routing.uploadLogFile() {
    post("/upload-log-file") {
        try {
            val parameters = call.parameters
            val logFileType = parameters["logFileType"] ?: DEFAULT_FOLDER
            val researchId = parameters.getOrFail<Int>("id")
            val logFile = createLogFile(logFileType, researchId)
            CoroutineScope(Job()).launch {
                logFile.parseLogFile(logFileType, researchId)
            }
            call.respond(HttpStatusCode.OK)
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        } catch (e: ParameterConversionException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        }
    }
}
