package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.testFramework.builders.ModuleFixtureBuilder
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetbrains.research.tasktracker.tracking.logger.DocumentLogger
import java.io.File

class TaskDocumentListenerTest : CodeInsightFixtureTestCase<ModuleFixtureBuilder<*>>() {

    fun testTaskDocumentListener() {
        val (logFile, document) = prepareDocument()
        linesToWrite.forEach {
            document.writeText(it)
        }
        val text = linesToWrite.joinToString(separator = "")
        ReadAction.run<Exception> {
            assert(document.text == text) { "Expected '$text' to be written, but found '${document.text}'" }
        }
        document.reWriteText("test")
        DocumentLogger.getDocumentLogPrinter(document)?.getActiveLogPrinter(project, document)?.csvPrinter?.flush()
        assert(logFile.exists()) { "log file with path '${logFile.path}' should have been created" }
        val changes = logFile.readLines()
            .filterIndexed { index, _ -> index > 1 }
            .map {
                it.split(",")[5]
                    .replace("\"", "")
            }
        /*
         we do not take the first 2 elements because first is column title
         and second is empty string with initial state of the file
         5 - is the column in csv with changes in file  */
        assert(changes == expectedChanges)
    }

    private fun prepareDocument(): Pair<File, Document> {
        val directory = File("${project.basePath}/${MainTaskTrackerConfig.PLUGIN_NAME}/cpp/")
        directory.mkdirs()
        val virtualFile = "${directory.path}/task.cpp".getVirtualFile()
        val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            ?: error("document with path '${virtualFile.path}' must exist")
        val logFileName =
            "${virtualFile.nameWithoutExtension}_${virtualFile.hashCode()}_${document.hashCode()}_0.csv"
        val logFile = File("${MainTaskTrackerConfig.logFilesFolder}/$logFileName")
        document.addDocumentListener(TaskDocumentListener(project))
        assert(!logFile.exists()) {
            "log file with path '${logFile.path}' should be created on first event in TaskDocumentListener"
        }
        return Pair(logFile, document)
    }

    private fun Document.writeText(text: String) {
        WriteCommandAction.runWriteCommandAction(project) {
            this.insertString(this.getLineEndOffset(0), text)
        }
        ReadAction.run<Exception> {
            assert(this.text.endsWith(text)) { "Expected $text to be in the end, but found ${this.text}" }
        }
    }

    private fun Document.reWriteText(text: String) {
        WriteCommandAction.runWriteCommandAction(project) {
            this.setText(text)
        }
        ReadAction.run<Exception> {
            assert(this.text == text) { "Expected $text to be written, but found ${this.text}" }
        }
    }

    companion object {
        private val linesToWrite = listOf("hello ", "world", "!")
        private val expectedChanges =
            linesToWrite.fold(emptyList<String>()) { list, current ->
                list + (list.lastOrNull().orEmpty() + current)
            } // stages of change
    }
}
