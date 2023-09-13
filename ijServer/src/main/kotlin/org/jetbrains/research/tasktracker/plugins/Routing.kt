@file:Suppress("NoWildcardImports", "UnsafeCallOnNullableType")

package org.jetbrains.research.tasktracker.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.currentResearchId
import org.jetbrains.research.tasktracker.currentUserId
import org.jetbrains.research.tasktracker.database.models.ActivityFileTable
import org.jetbrains.research.tasktracker.database.models.DocumentFileTable
import org.jetbrains.research.tasktracker.database.models.ResearchTable
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun Application.configureRouting() {
    routing {
        get("/get-user-id") {
            call.respondText(currentUserId.toString())
        }
        get("/get-research-id") {
            call.respondText(currentResearchId.toString())
        }

        post("/create-research") {
            val formParameters = call.receiveParameters()
            transaction {
                ResearchTable.insert {
                    it[id] = formParameters["id"]!!.toInt()
                    it[userId] = formParameters["user_id"]!!.toInt()
                    it[name] = formParameters["name"]!!
                }
            }
            call.respond(HttpStatusCode.Accepted)
        }

        post("upload-activity/{id}") {
            this.createLogFile(
                "activity"
            ) { name, index ->
                run {
                    transaction {
                        ActivityFileTable.insert {
                            it[ActivityFileTable.name] = name
                            it[researchId] = index
                        } get ActivityFileTable.id
                    }
                }
            }
            call.respond(HttpStatusCode.Accepted)
        }

        post("upload-document/{id}") {
            this.createLogFile(
                "activity"
            ) { name, index ->
                run {
                    transaction {
                        DocumentFileTable.insert {
                            it[DocumentFileTable.name] = name
                            it[researchId] = index
                        } get DocumentFileTable.id
                    }
                }
            }
            call.respond(HttpStatusCode.Accepted)
        }
    }
}

suspend inline fun PipelineContext<Unit, ApplicationCall>.createLogFile(
    subDirectory: String,
    crossinline insert: (name: String, researchId: Int) -> Unit
) {
    val multipartData = call.receiveMultipart()
    val researchIndex = call.parameters["id"]?.toInt() ?: 0 // TODO
    multipartData.forEachPart { part ->
        when (part) {
            is PartData.FileItem -> {
                val fileName = part.originalFileName as String
                val fileBytes = part.streamProvider().readBytes()
                insert(fileName, researchIndex)
                getFile(researchIndex, fileName, subDirectory).writeBytes(fileBytes)
            }

            else -> {}
        }
        part.dispose()
    }
}

fun getFile(researchId: Int, filename: String, subDirectory: String): File {
    val directoryPath = "files/$researchId/$subDirectory"
    Files.createDirectories(Paths.get(directoryPath))
    return File("$directoryPath/$filename").also {
        it.createNewFile()
        println(it.absolutePath)
    }
}
