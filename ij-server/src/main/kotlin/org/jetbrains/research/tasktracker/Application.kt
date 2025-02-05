package org.jetbrains.research.tasktracker

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.research.tasktracker.database.DatabaseFactory
import org.jetbrains.research.tasktracker.plugins.configureRouting

fun main() {
    embeddedServer(
        Netty,
        port = getEnvOrNull("PORT")?.toIntOrNull() ?: 8080,
        host = getEnvOrNull("HOST") ?: "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureRouting()
}

fun getEnvOrNull(name: String): String? {
    try {
        return System.getenv(name)
    } catch (_: Exception) {}
    return null
}
