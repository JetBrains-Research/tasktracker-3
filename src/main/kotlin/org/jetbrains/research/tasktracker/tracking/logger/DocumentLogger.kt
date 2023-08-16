package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document

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
        getDocumentLogPrinter(document)?.getLogFiles()
            ?: logger.error("attempt to flush non-existing csv printer for document '$document'")
        myDocumentsToPrinters.remove(document)
    }
}
