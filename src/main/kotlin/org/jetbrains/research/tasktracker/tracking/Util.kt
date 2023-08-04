package org.jetbrains.research.tasktracker.tracking

import java.util.*

fun String.toPackageName() =
    listOf(" ", "-", "_").fold(this) { acc, s -> acc.replace(s, "") }.lowercase(Locale.getDefault())
