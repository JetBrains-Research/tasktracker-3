package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.currentUserId
import org.jetbrains.research.tasktracker.database.models.ResearchTable

fun Routing.getUserId() {
    get("/get-user-id") {
        call.respondText(currentUserId.toString())
        call.respond(HttpStatusCode.Accepted)
    }
}

fun Routing.createResearch() {
    post("/create-research") {
        val formParameters = call.receiveParameters()
        transaction {
            ResearchTable.insert {
                it[id] = formParameters.getOrFail<Int>("id")
                it[userId] = formParameters.getOrFail<Int>("user_id")
                it[name] = formParameters.getOrFail("name")
            }
        }
        call.respond(HttpStatusCode.Accepted)
    }
}
