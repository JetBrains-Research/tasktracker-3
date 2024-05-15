@file:ImportDataSchema(
    "Repository",
    "https://raw.githubusercontent.com/Kotlin/dataframe/master/data/jetbrains_repositories.csv",
)

package org.jetbrains.research.tasktracker

import kotlinx.datetime.LocalDateTime
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.dataframe.size
import java.io.File
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory
import kotlin.io.path.exists


var counter = 1

//TODO
private var activityData =
    DataFrame.readCSV("processing/src/main/resources/tt_files/activitydata.csv")
        .add("data_type") { "Activity" }
private var documentData =
    DataFrame.readCSV("processing/src/main/resources/tt_files/documentdata.csv")
private var fileEditorData =
    DataFrame.readCSV("processing/src/main/resources/tt_files/fileeditordata.csv")
        .add("data_type") { "fileEditorData" }
private var researches =
    DataFrame.readCSV("processing/src/main/resources/tt_files/researches.csv")
private var surveyData =
    DataFrame.readCSV("processing/src/main/resources/tt_files/surveyData.csv")
private var toolWindowData =
    DataFrame.readCSV("processing/src/main/resources/tt_files/toolwindowdata.csv")
        .add("data_type") { "toolWindowData" }
private var users =
    DataFrame.readCSV("processing/src/main/resources/tt_files/users.csv")

val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS XXX")

fun getAllActivityData() =
    activityData.concat(fileEditorData).concat(toolWindowData).dropNA("date")
        .update("date")
        .with { OffsetDateTime.parse(it.toString(), dateFormatter).toString() }
        .join(researches) { "research_id" match "id" }
        .convert("date")
        .toLocalDateTime()
        .sortBy("date")

fun getAllDocumentData() =
    documentData.dropNA("date")
        .update("date")
        .with { OffsetDateTime.parse(it.toString(), dateFormatter).toString() }
        .join(researches) { "research_id" match "id" }
        .convert("date")
        .toLocalDateTime()

data class FilesDirectory(val date: LocalDateTime, val researchId: String, val directory: Int)

val directory = File("test-processing")

fun writeToFiles(acc: Collection<DataRow<*>>) = acc.forEach { fileRow ->
    val fileName = fileRow["filename"].toString().split("/", "\\").last()
    val dir = directory.toPath().resolve(counter.toString())
    if (!dir.exists()) {
        dir.createDirectories()
    }
    dir.resolve(fileName).toFile().let {
        if (!it.exists()) {
            it.createNewFile()
            it.writeText(fileRow["fragment"].toString())
        }
    }
}

fun getCodeDataFrame(): DataFrame<*> {
    val files = mutableSetOf<FilesDirectory>()
    val allDocuments = getAllDocumentData()
    allDocuments.groupBy("research_id").forEach {
        val acc = mutableSetOf<DataRow<*>>()
        val sortBy = it.group.sortBy("date")
        sortBy.rows().forEach { row ->
            if (acc.find { it["filename"] == row["filename"] } == null)
                acc.add(row)
        }
        val date = acc.minOf { it.get("date") as LocalDateTime }
        writeToFiles(acc)
        files.add(FilesDirectory(date, it.key.toString(), counter++))
        sortBy.rows().forEach { row ->
            if (row !in acc) {
                acc.removeIf { it["filename"] == row["filename"] }
                acc.add(row)
                //TODO write to files
                writeToFiles(acc)
                files.add(FilesDirectory(row["date"] as LocalDateTime, it.key.toString(), counter++))
            }
        }
    }
    return dataFrameOf(
        listOf("date", "research_id", "directory"),
        files.flatMap { listOf(it.date, it.researchId, it.directory) })
}

fun main() {

}
