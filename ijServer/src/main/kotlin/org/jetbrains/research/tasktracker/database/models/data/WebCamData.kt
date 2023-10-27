package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.InsertStatement

object WebCamData : DataTable() {
    val emotionShown = text("emotion_shown")
    val isRegular = bool("is_regular")
    val scores = text("scores")

    override fun insertData(insertStatement: InsertStatement<Number>, iterator: Iterator<String>, researchId: Int) {
        insertStatement[emotionShown] = iterator.next()
        insertStatement[isRegular] = iterator.next().toBoolean()
        insertStatement[scores] = iterator.next()
    }
}
