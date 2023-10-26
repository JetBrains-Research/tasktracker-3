package org.jetbrains.research.tasktracker.database.models.data

object WebCamData : DataTable() {
    val emotionShown = text("emotion_shown")
    val isRegular = bool("is_regular")
    val scores = text("scores")
}
