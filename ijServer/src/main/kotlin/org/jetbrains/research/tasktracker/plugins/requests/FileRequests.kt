package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.createLogFile

private const val DEFAULT_FOLDER = "default"

fun Routing.uploadFile() {
    post("upload-document/{id}") {
        this.createLogFile(
            call.request.queryParameters["subdir"] ?: DEFAULT_FOLDER
        ) { name, index ->
            run {
                transaction {
                }
            }
        }
        call.respond(HttpStatusCode.Accepted)
    }
}
