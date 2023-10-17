package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.Research

fun Routing.createResearch() {
    post("/create-research") {
        val formParameters = call.receiveParameters()
        transaction {
            Research.new {
                name = formParameters.getOrFail("name")
            }
        }
        call.respond(HttpStatusCode.Accepted)
    }
}
