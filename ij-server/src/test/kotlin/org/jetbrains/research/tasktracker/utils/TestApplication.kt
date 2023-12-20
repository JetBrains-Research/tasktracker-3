package org.jetbrains.research.tasktracker.utils

import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.Researches
import org.jetbrains.research.tasktracker.database.models.Users
import org.jetbrains.research.tasktracker.database.models.data.*
import org.jetbrains.research.tasktracker.plugins.configureRouting

fun testApplicationRouted(block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
    application {
        configureRouting()
    }
    val tables = arrayOf(
        Users,
        Researches,
        DocumentData,
        ActivityData,
        WebCamData,
        ToolWindowData,
        SurveyData,
        FileEditorData
    )
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;IGNORECASE=true;", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(*tables)
    }
    block()
}

suspend fun HttpClient.createUserRequest(name: String, email: String) = submitForm(
    url = "/create-user",
    formParameters = parameters {
        append("name", name)
        append("email", email)
    }
)

suspend fun HttpClient.createResearchRequest(name: String, userId: Int, description: String? = null) =
    submitForm(
        url = "/create-research",
        formParameters = parameters {
            append("name", name)
            description?.let {
                append("description", description)
            }
            append("user_id", userId.toString())
            append("research_unique_id", "test")
        }
    )
