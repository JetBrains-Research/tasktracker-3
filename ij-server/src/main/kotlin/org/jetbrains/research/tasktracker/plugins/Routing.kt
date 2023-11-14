package org.jetbrains.research.tasktracker.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.research.tasktracker.plugins.requests.createResearch
import org.jetbrains.research.tasktracker.plugins.requests.createUser
import org.jetbrains.research.tasktracker.plugins.requests.ping
import org.jetbrains.research.tasktracker.plugins.requests.uploadLogFile

fun Application.configureRouting() {
    routing {
        createUser()
        createResearch()
        uploadLogFile()
        ping()
    }
}
