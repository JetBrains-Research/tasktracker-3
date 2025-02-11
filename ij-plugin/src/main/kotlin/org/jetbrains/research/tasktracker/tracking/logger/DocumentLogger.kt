package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.research.tasktracker.requests.FileRequests
import java.io.File

object DocumentLogger {
    private val myDocumentsToPrinters: HashMap<Document, DocumentLogPrinter> = HashMap()
    private val logger: Logger = Logger.getInstance(DocumentLogger.javaClass)

    fun log(project: Project, document: Document) {
        val docPrinter = myDocumentsToPrinters.getOrPut(document) { DocumentLogPrinter() }
        val logPrinter = docPrinter.getActiveLogPrinter(project, document)
        runWriteAction { logPrinter.csvPrinter.printRecord(DocumentLoggedData(project).getData(document)) }
    }

    fun getDocumentLogPrinter(document: Document): DocumentLogPrinter? = myDocumentsToPrinters[document]

    fun removeDocumentLogPrinter(project: Project, document: Document) {
        log(project, document)
        val logFiles: List<File> = getDocumentLogPrinter(document)?.getLogFiles()
            ?: emptyList<File>().also {
                logger.error("attempt to flush non-existing csv printer for document '$document'")
            }
        logFiles.forEach {
            GlobalScope.launch {
                FileRequests.sendFile(it, "document")
            }
        }
        myDocumentsToPrinters.remove(document)
    }
}
