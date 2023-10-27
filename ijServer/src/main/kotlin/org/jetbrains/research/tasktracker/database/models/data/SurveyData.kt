package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

object SurveyData : DataTable() {
    val questionId = integer("question_id")
    val question = text("question")
    val option = text("option").nullable()
    val answer = text("answer")

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[questionId] = iterator.next().toInt()
        insertStatement[question] = iterator.next()
        insertStatement[option] = iterator.next()
        insertStatement[answer] = iterator.next()
    }
}
