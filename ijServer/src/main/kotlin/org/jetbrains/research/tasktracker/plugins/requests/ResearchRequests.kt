package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.Research
import org.jetbrains.research.tasktracker.database.models.User

fun Routing.createResearch() {
    post("/create-research") {
        val formParameters = call.receiveParameters()
        try {
            val researchId = transaction {
                Research.new {
                    name = formParameters.getOrFail("name")
                    description = formParameters["description"]
                    user = User[formParameters.getOrFail<Int>("user_id")]
                }.id.value
            }
            call.respondText(
                researchId.toString(),
                status = HttpStatusCode.Created
            )
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        } catch (e: ParameterConversionException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        }
    }
}
