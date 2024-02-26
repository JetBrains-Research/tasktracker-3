package org.jetbrains.research.tasktracker.database.models.data

import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * The table represents information about emotions determined through photos.
 */
object WebCamData : DataTable() {

    /**
     * Emotion name.
     */
    val emotionShown = text("emotion_shown")

    /**
     * Value - true if the fixation of emotion was scheduled
     * (Emotion fixation with a certain periodicity), false otherwise.
     */
    val isRegular = bool("is_regular")

    /**
     * The probabilities of all available emotions when determining an emotion.
     */
    val scores = text("scores")

    override fun insertData(updateBuilder: UpdateBuilder<*>, iterator: Iterator<String>, researchId: Int) {
        updateBuilder[emotionShown] = iterator.next()
        updateBuilder[isRegular] = iterator.next().toBoolean()
        updateBuilder[scores] = iterator.next()
    }
}
