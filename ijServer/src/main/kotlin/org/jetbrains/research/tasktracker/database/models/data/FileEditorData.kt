package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.research.tasktracker.database.models.data.ActivityData.nullable

object FileEditorData : DataTable() {
    val action = text("action")
    val oldFile = text("old_file").nullable()
    val newFile = text("new_file").nullable()
}
