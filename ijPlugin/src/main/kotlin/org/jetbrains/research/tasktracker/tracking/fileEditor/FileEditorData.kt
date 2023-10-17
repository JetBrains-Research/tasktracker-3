package org.jetbrains.research.tasktracker.tracking.fileEditor

import org.joda.time.DateTime

data class FileEditorData(
    val time: DateTime,
    val fileEditorAction: FileEditorAction,
    val oldFileName: String? = null,
    val newFileName: String? = null
)
