package org.jetbrains.research.tasktracker.plugins.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.User
import org.jetbrains.research.tasktracker.database.models.Users

fun Routing.createUser() {
    post("/create-user") {
        val formParameters = call.receiveParameters()
        try {
            val userId = transaction {
                val name = formParameters.getOrFail("name")
                val email = formParameters.getOrFail("email")
                getUserId(name, email)
            }
            call.respondText(
                userId.toString(),
                status = HttpStatusCode.Created
            )
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        } catch (e: MissingRequestParameterException) {
            call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
        }
    }
}

@Suppress("SwallowedException")
fun getUserId(name: String, email: String): Int {
    val user = User.find { (Users.name eq name) and (Users.email eq email) }
    if (user.empty()) {
        try {
            return User.new {
                this.name = name
                this.email = email
            }.id.value
        } catch (e: ExposedSQLException) {
            throw IllegalArgumentException("user with name `$name` or with email `$email` already exists")
        }
    }
    return user.first().id.value
}
