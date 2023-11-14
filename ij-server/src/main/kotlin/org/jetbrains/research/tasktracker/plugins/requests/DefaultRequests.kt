package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Routing checking that the server is running.
 */
fun Routing.ping() {
    get("/healthz") {
        call.respondText("OK")
    }
}
