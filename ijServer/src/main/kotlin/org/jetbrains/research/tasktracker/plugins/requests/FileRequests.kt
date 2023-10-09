package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.createLogFile
import org.jetbrains.research.tasktracker.database.models.ActivityFileTable

fun Routing.uploadFile(){
    post("upload-document/{id}") { // TODO rename path
        this.createLogFile(
            call.request.queryParameters["subdir"] ?: "default"
        ) { name, index ->
            run {
                transaction {
                    ActivityFileTable.insert {
                        it[ActivityFileTable.name] = name
                        it[researchId] = index
                    } get ActivityFileTable.id
                }
            }
        }
        call.respond(HttpStatusCode.Accepted)
    }
}
