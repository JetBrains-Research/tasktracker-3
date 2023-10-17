package org.jetbrains.research.tasktracker

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*
import org.jetbrains.research.tasktracker.database.models.Research
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories

suspend inline fun PipelineContext<Unit, ApplicationCall>.createLogFile(
    subDirectory: String,
    crossinline insert: (name: String, researchId: Int) -> Unit
) {
    val multipartData = call.receiveMultipart()
    val researchIndex = call.parameters.getOrFail<Int>("id")
    multipartData.forEachPart { part ->
        if (part is PartData.FileItem) {
            val fileName = part.originalFileName as String
            val fileBytes = part.streamProvider().readBytes()
            insert(fileName, researchIndex)
            val directoryPath = createDirectoryPath(researchIndex, subDirectory)
            getAndCreateFile(directoryPath, fileName).writeBytes(fileBytes)
        }
        part.dispose()
    }
    call.respond(HttpStatusCode.Accepted)
}

fun createDirectoryPath(researchId: Int, subDirectory: String): Path {
    val userId = Research.findById(researchId)?.userId
        ?: error("There are no research with id `$researchId`")
    val directoryPath = Paths.get("files/$researchId/$userId/$subDirectory")
    directoryPath.createDirectories()
    return directoryPath
}

fun getAndCreateFile(root: Path, filename: String): File {
    return root.resolve(filename).toFile().also { it.createNewFile() }
}
