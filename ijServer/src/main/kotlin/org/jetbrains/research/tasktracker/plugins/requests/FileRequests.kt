package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.research.tasktracker.util.createLogFile
import org.jetbrains.research.tasktracker.util.logFile.parseLogFile

private const val DEFAULT_FOLDER = "default"

fun Routing.uploadLogFile() {
    post("/upload-log-file") {
        try {
            val logFileType = call.request.queryParameters["logFileType"] ?: DEFAULT_FOLDER
            val researchId = call.parameters.getOrFail<Int>("id")
            val logFile = createLogFile(logFileType, researchId)
            logFile.parseLogFile(logFileType, researchId)
            call.respond(HttpStatusCode.OK)
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        } catch (e: ParameterConversionException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        }
    }
}
