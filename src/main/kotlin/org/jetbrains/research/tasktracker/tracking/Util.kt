package org.jetbrains.research.tasktracker.tracking

fun String.toPackageName() =
    listOf(" ", "-", "_").fold(this) { acc, s -> acc.replace(s, "") }
