package org.jetbrains.research.tasktracker.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.research.tasktracker.plugins.requests.createResearch
import org.jetbrains.research.tasktracker.plugins.requests.getResearchId
import org.jetbrains.research.tasktracker.plugins.requests.getUserId
import org.jetbrains.research.tasktracker.plugins.requests.uploadFile

fun Application.configureRouting() {
    routing {
        getUserId()
        getResearchId()
        createResearch()
        uploadFile()
    }
}
