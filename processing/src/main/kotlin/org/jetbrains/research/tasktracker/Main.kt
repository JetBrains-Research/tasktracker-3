@file:ImportDataSchema(
    "Repository",
    "https://raw.githubusercontent.com/Kotlin/dataframe/master/data/jetbrains_repositories.csv",
)

package org.jetbrains.research.tasktracker

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.readCSV
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


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
        .join(researches){ "research_id" match "id" }
        .convert("date")
        .toLocalDateTime().sortBy("date")


fun main() {
    val allActivity = getAllActivityData()
    allActivity.print()
}
