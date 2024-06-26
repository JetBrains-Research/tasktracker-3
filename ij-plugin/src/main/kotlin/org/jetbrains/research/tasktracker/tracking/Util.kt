package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.models.Extension
import java.util.*

fun String.toPackageName() =
    listOf(" ", "-", "_").fold(this) { acc, s -> acc.replace(s, "") }

fun Extension.getDirectoryName() = this.name.lowercase(Locale.getDefault())
