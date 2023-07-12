package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.editor.Document

object DocumentLogger {
    private val myDocumentsToPrinters: HashMap<Document, DocumentLogPrinter> = HashMap()

    fun log(document: Document) {
        val docPrinter = myDocumentsToPrinters.getOrPut(document) { DocumentLogPrinter() }
        val logPrinter = docPrinter.getActiveLogPrinter(document)
        logPrinter.csvPrinter.printRecord(DocumentLoggedData.getData(document))
    }

    fun getDocumentLogPrinter(document: Document): DocumentLogPrinter? = myDocumentsToPrinters[document]

    fun removeDocumentLogPrinter(document: Document) {
        myDocumentsToPrinters.remove(document)
    }
}
