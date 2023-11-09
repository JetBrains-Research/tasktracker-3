package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

/**
 * The table represents information about a completed survey.
 */
object SurveyData : DataTable() {
    val questionId = integer("question_id")

    /**
     * Question text
     */
    val question = text("question")

    /**
     * In the context of a radio button, this is the ID of the selected option.
     */
    val option = text("option").nullable()
    val answer = text("answer")

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[questionId] = iterator.next().toInt()
        insertStatement[question] = iterator.next()
        insertStatement[option] = iterator.next()
        insertStatement[answer] = iterator.next()
    }
}
