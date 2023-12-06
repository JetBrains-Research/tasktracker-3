package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import org.jetbrains.research.tasktracker.requests.FileRequests
import java.io.File

object DocumentLogger {
    private val myDocumentsToPrinters: HashMap<Document, DocumentLogPrinter> = HashMap()
    private val logger: Logger = Logger.getInstance(DocumentLogger.javaClass)

    fun log(document: Document) {
        val docPrinter = myDocumentsToPrinters.getOrPut(document) { DocumentLogPrinter() }
        val logPrinter = docPrinter.getActiveLogPrinter(document)
        logPrinter.csvPrinter.printRecord(DocumentLoggedData.getData(document))
    }

    fun getDocumentLogPrinter(document: Document): DocumentLogPrinter? = myDocumentsToPrinters[document]

    fun removeDocumentLogPrinter(document: Document) {
        log(document)
        val logFiles: List<File> = getDocumentLogPrinter(document)?.getLogFiles()
            ?: emptyList<File>().also {
                logger.error("attempt to flush non-existing csv printer for document '$document'")
            }
        logFiles.all {
            FileRequests.sendFile(it, "document")
        }
        myDocumentsToPrinters.remove(document)
    }
}
