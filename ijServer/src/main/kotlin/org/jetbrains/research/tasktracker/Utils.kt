package org.jetbrains.research.tasktracker

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.research.tasktracker.database.models.ResearchTable
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger

private lateinit var currentUserIdAtomic: AtomicInteger
private lateinit var currentResearchIdAtomic: AtomicInteger
private lateinit var currentSurveyIdAtomic: AtomicInteger

val currentUserId: Int
    get() = currentResearchIdAtomic.getAndIncrement()

val currentResearchId: Int
    get() = currentResearchIdAtomic.getAndIncrement()

val currentSurveyId: Int
    get() = currentResearchIdAtomic.getAndIncrement()

fun initializeIds() {
    currentUserIdAtomic = AtomicInteger(ResearchTable.maxUserId() + 1)
    currentResearchIdAtomic = AtomicInteger(ResearchTable.maxId() + 1)
    currentSurveyIdAtomic = AtomicInteger(0)
}

suspend inline fun PipelineContext<Unit, ApplicationCall>.createLogFile(
    subDirectory: String,
    crossinline insert: (name: String, researchId: Int) -> Unit
) {
    val multipartData = call.receiveMultipart()
    val researchIndex = call.parameters.getOrFail<Int>("id")
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
    call.respond(HttpStatusCode.Accepted)
}

fun getFile(researchId: Int, filename: String, subDirectory: String): File {
    val userId = transaction { ResearchTable.select { ResearchTable.id.eq(researchId) }.first()[ResearchTable.userId] }
    val directoryPath = "files/$userId/$researchId/$subDirectory"
    Files.createDirectories(Paths.get(directoryPath))
    return File("$directoryPath/$filename").also {
        it.createNewFile()
    }
}
