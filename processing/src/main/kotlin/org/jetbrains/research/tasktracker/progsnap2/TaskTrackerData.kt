package org.jetbrains.research.tasktracker.progsnap2

import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import java.io.File
import java.util.*

data class TaskTrackerData(
    val activityData: File,
    val documentData: File,
    val fileEditorData: File,
    val researches: File,
    val toolWindowData: File,
    val metaData: ProgsnapMeta
)

data class ProgsnapMeta(
    val version: Int,
    val codeStateRepresentation: CodeStateRepresentation,
    val eventOrderScope: EventOrderScope = EventOrderScope.NONE,
    val isEventOrderingConsistent: Boolean = false,
    val eventOrderScopeColumns: String = ""
) {
    fun toDataFrame() = dataFrameOf("Property", "Value")(
        "Version", version,
        "IsEventOrderingConsistent", isEventOrderingConsistent.toString().lowercase(Locale.getDefault()),
        "EventOrderScope", eventOrderScope.toString().toValueName(),
        "EventOrderScopeColumns", eventOrderScopeColumns,
        "CodeStateRepresentation", codeStateRepresentation.toString().toValueName(),
    )
}

enum class EventOrderScope {
    NONE,
    GLOBAL,
    RESTRICTED
}

enum class CodeStateRepresentation {
    TABLE,
    DIRECTORY,
    GIT
}

fun String.toValueName() =
    lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
