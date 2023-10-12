package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.research.tasktracker.currentResearchId

fun Routing.getResearchId() {
    get("/get-research-id") {
        call.respondText(currentResearchId.toString())
        call.respond(HttpStatusCode.Accepted)
    }
}
