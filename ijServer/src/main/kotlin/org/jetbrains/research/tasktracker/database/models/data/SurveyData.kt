package org.jetbrains.research.tasktracker.database.models.data

object SurveyData: DataTable() {
    val questionId = integer("question_id")
    val question = text("question")
    val option = text("option").nullable()
    val answer = text("answer")
}