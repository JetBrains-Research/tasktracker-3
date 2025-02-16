package org.jetbrains.research.tasktracker.progsnap2

import kotlinx.datetime.LocalDateTime
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import java.io.File
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

@Suppress("TooManyFunctions")
class ProgsnapParser(private val taskTrackerData: TaskTrackerData) {

    companion object {
        private val DATE_FORMAT_WITH_MICROS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX")
        private val DATE_WITHOUT_MICROS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX")
        private val DATE_FORMATS = listOf(DATE_FORMAT_WITH_MICROS, DATE_WITHOUT_MICROS)
        private val DIRECTORIES_DELIMITERS = arrayOf("/", "\\")
        private const val CODE_STATES_DIRECTORY_NAME = "CodeStates"
        private const val MAIN_TABLE_FILENAME = "MainTable.csv"
        private const val META_DATA_FILENAME = "DatasetMetadata.csv"
    }

    fun convert(destinationDirectory: File) {
        require(destinationDirectory.exists()) { "destination directory $destinationDirectory doesn't exist" }
        createMetaFile(destinationDirectory)
        val researchGroups = getCodeDataFrame(destinationDirectory).groupBy("researchId")
        createMainTableFile(destinationDirectory, researchGroups)
    }

    private fun parseDate(dateString: String): OffsetDateTime {
        for (formatter in DATE_FORMATS) {
            try {
                return OffsetDateTime.parse(dateString, formatter)
            } catch (_: DateTimeParseException) {
                // Ignore and try the next format
            }
        }
        error("Invalid date format: $dateString")
    }

    private fun createMetaFile(destinationDirectory: File) {
        val metaDataFile = destinationDirectory.resolve(META_DATA_FILENAME)
        taskTrackerData.metaData.toDataFrame().writeCSV(metaDataFile)
    }

    private fun createMainTableFile(destinationDirectory: File, researchGroups: GroupBy<*, *>) {
        var id = 1
        val activityData = getAllActivityData()
        activityData.mapToFrame {
            "EventID" from { id++ }
            "subjectID" from { it["user"] }
            "EventType" from { it.getEventType() }
            "ToolInstance" from { "Kotlin" }
            "CourseId" from { it["research_unique_id"] }
            "CodeStateID" from {
                researchGroups.getDirectoryByDateAndResearch(
                    it["date"] as LocalDateTime,
                    it["research_id"].toString()
                )
            }
        }.dropNA().writeCSV(destinationDirectory.resolve(MAIN_TABLE_FILENAME))
    }

    private var activityData =
        taskTrackerData.activityData.toDataFrame()
            .add("data_type") { "activityData" }
    private var documentData =
        taskTrackerData.documentData.toDataFrame()
    private var fileEditorData =
        taskTrackerData.fileEditorData.toDataFrame()
            .add("data_type") { "fileEditorData" }
    private var researches =
        taskTrackerData.researches.toDataFrame()
            .convert("id").to<Int>()
    private var toolWindowData =
        taskTrackerData.toolWindowData.toDataFrame()
            .add("data_type") { "toolWindowData" }

    private fun File.toDataFrame() = DataFrame.readCSV(this)

    private var directoryCounter = 1

    private fun DataFrame<*>.convertDate() = this
        .dropNA("date")
        .update("date")
        .with { parseDate(it.toString()).toString() }
        .convert("research_id").to<Int>()
        .fullJoin(researches) { "research_id" match "id" }
        .dropNA("date")
        .convert("date")
        .toLocalDateTime()

    private fun getAllActivityData() =
        activityData
            .concat(fileEditorData)
            .concat(toolWindowData)
            .convertDate()
            .sortBy("date")

    private fun getAllDocumentData() =
        documentData.convertDate()

    private fun writeToFiles(codeState: Collection<DataRow<*>>, destinationDirectory: File) =
        codeState.forEach { fileRow ->
            val relativePathElements = fileRow["filename"].toString().split(delimiters = DIRECTORIES_DELIMITERS)
            val fileName = relativePathElements.last()
            val directories = relativePathElements.take(relativePathElements.size - 1)
                .joinToString(separator = "/") // do not take filename
            val directoryToWrite =
                destinationDirectory.toPath().resolve(CODE_STATES_DIRECTORY_NAME).resolve(directoryCounter.toString())
                    .resolve(directories)
            if (!directoryToWrite.exists()) {
                directoryToWrite.createDirectories()
            }
            directoryToWrite.resolve(fileName).toFile().let { file ->
                if (!file.exists()) {
                    file.createNewFile()
                    file.writeText(fileRow["fragment"].toString())
                }
            }
        }

    private fun getCodeDataFrame(destinationDirectory: File): DataFrame<*> {
        val codeStates = mutableSetOf<FilesDirectory>()
        val allDocuments = getAllDocumentData()
        allDocuments.groupBy("research_id").forEach { entry ->
            val currentFileState = mutableSetOf<DataRow<*>>()
            val sortedGroup = entry.group.sortBy("date")
            sortedGroup.rows().forEach { row ->
                if (!currentFileState.any { it["filename"] == row["filename"] }) {
                    currentFileState.add(row)
                }
            }
            val date = currentFileState.minOf { it["date"] as LocalDateTime }
            writeToFiles(currentFileState, destinationDirectory) // write first state of files in the research
            codeStates.addNewState(date, entry)
            sortedGroup.rows().forEach { row ->
                if (row !in currentFileState) {
                    currentFileState.removeIf { it["filename"] == row["filename"] }
                    currentFileState.add(row)
                    writeToFiles(currentFileState, destinationDirectory)
                    codeStates.addNewState(row["date"] as LocalDateTime, entry)
                }
            }
        }
        return codeStates.toDataFrame()
    }

    @Suppress("CyclomaticComplexMethod")
    private fun DataRow<*>.getEventType(): String {
        return when (val dataType = this["data_type"]) {
            "activityData" -> {
                when (val type = this["type"]) {
                    "Execution" -> {
                        val info = this["info"]
                        when {
                            info.toString().startsWith("Run") -> "Run.Program"
                            info.toString().startsWith("Debug") -> "Debug.Program"
                            else -> error("Undefined Execution type has been detected in the info `$info`")
                        }
                    }

                    "Shortcut" -> "X-Shortcut"
                    "KeyPressed" -> "X-KeyPressed"
                    "KeyReleased" -> "X-KeyReleased"
                    "Action" -> "X-Action"
                    "MouseClicked" -> "X-MouseClicked"
                    "MouseMoved" -> "X-MouseMoved"
                    "MouseWheel" -> "X-MouseWheel"
                    else -> error("Undefined activity data type has been detected in the data `$type`")
                }
            }

            "fileEditorData" -> when (val action = this["action"]) {
                "FOCUS" -> "File.Focus"
                "OPEN" -> "File.Open"
                "CLOSE" -> "File.Close"
                else -> error("Undefined action of file editor type has been detected: `$action`")
            }

            "toolWindowData" -> when (val action = this["action"]) {
                "FOCUSED" -> "X-Toolwindow.Focus"
                "OPENED" -> "X-Toolwindow.Open"
                else -> error("Undefined action of tool window type has been detected: `$action`")
            }

            else -> error("Undefined datatype: `$dataType`")
        }
    }

    private fun GroupBy<*, *>.getDirectoryByDateAndResearch(date: LocalDateTime, researchId: String): Int? {
        val key = keys.firstOrNull { it["researchId"] == researchId } ?: return null
        val rows = groups[key.index()].rows().reversed()
        val index = rows.indexOfFirst { date > it["date"] as LocalDateTime }
        return if (index == -1) rows.first()["directory"] as Int else rows.elementAt(index)["directory"] as Int
    }

    private fun MutableSet<FilesDirectory>.addNewState(date: LocalDateTime, entry: GroupBy.Entry<Any?, Any?>) = add(
        FilesDirectory(date, entry.key.values().first().toString(), directoryCounter++)
    )

    private data class FilesDirectory(val date: LocalDateTime, val researchId: String, val directory: Int)
}
