package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.research.tasktracker.util.createLogFile
import org.jetbrains.research.tasktracker.util.parseLogFile

private const val DEFAULT_FOLDER = "default"

fun Routing.uploadLogFile() {
    post("upload-log-file/") {
        val logFileType = call.request.queryParameters["logFileType"] ?: DEFAULT_FOLDER
        val researchId = call.parameters.getOrFail<Int>("id")
        val logFile = createLogFile(logFileType)
        logFile.parseLogFile(logFileType, researchId)
        call.respond(HttpStatusCode.Accepted)
    }
}
