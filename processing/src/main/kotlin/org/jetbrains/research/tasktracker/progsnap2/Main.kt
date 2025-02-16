package org.jetbrains.research.tasktracker.progsnap2

import java.io.File

fun main(args: Array<String>) {
    val (trackedFilesDirectory, destinationDirectory) = gatherArguments(args)
    with(trackedFilesDirectory) {
        ProgsnapParser(
            TaskTrackerData(
                resolve(ACTIVITY_DATA_CSV),
                resolve(DOCUMENT_DATA_CSV),
                resolve(FILE_EDITOR_DATA_CSV),
                resolve(RESEARCHES_CSV),
                resolve(TOOL_WINDOW_DATA_CSV),
                ProgsnapMeta(
                    PROGSNAP_VERSION,
                    CodeStateRepresentation.DIRECTORY,
                    EventOrderScope.GLOBAL,
                    true,
                    "SubjectID"
                )
            )
        ).convert(destinationDirectory)
    }
}

private fun gatherArguments(args: Array<String>): Pair<File, File> {
    require(args.size == 2) { "Exactly 2 arguments are required: <csvFolder> <existingFolder>." }

    val csvFolder = File(args[0])
    require(csvFolder.isDirectory) { "The first argument must be a directory containing CSV files." }
    val missingFiles = expectedCsvFiles.filter { !File(csvFolder, it).exists() }
    require(missingFiles.isEmpty()) {
        "The following required CSV files are missing: ${missingFiles.joinToString(", ")}"
    }

    val existingDirectory = File(args[1])
    require(existingDirectory.isDirectory) { "The destination directory argument must be an existing directory." }

    return Pair(csvFolder, existingDirectory)
}

private const val ACTIVITY_DATA_CSV = "activitydata.csv"
private const val DOCUMENT_DATA_CSV = "documentdata.csv"
private const val FILE_EDITOR_DATA_CSV = "fileeditordata.csv"
private const val RESEARCHES_CSV = "researches.csv"
private const val TOOL_WINDOW_DATA_CSV = "toolwindowdata.csv"

private val expectedCsvFiles = listOf(
    ACTIVITY_DATA_CSV,
    DOCUMENT_DATA_CSV,
    FILE_EDITOR_DATA_CSV,
    RESEARCHES_CSV,
    TOOL_WINDOW_DATA_CSV
)

private const val PROGSNAP_VERSION = 5
