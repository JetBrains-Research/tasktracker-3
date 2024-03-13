package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.UpdateBuilder

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

    override fun insertData(updateBuilder: UpdateBuilder<*>, iterator: Iterator<String>, researchId: Int) {
        updateBuilder[questionId] = iterator.next().toInt()
        updateBuilder[question] = iterator.next()
        updateBuilder[option] = iterator.next()
        updateBuilder[answer] = iterator.next()
    }
}
