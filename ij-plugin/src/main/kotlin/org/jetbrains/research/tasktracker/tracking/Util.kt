package org.jetbrains.research.tasktracker.tracking

import org.jetbrains.research.tasktracker.models.Extension
import java.util.*

fun String.toPackageName() =
    listOf(" ", "-", "_").fold(this) { acc, s -> acc.replace(s, "") }.lowercase(Locale.getDefault())

fun Extension.getFolderName() = this.name.lowercase(Locale.getDefault())
