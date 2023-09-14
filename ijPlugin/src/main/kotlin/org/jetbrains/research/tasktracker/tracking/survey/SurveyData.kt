package org.jetbrains.research.tasktracker.tracking.survey

import org.joda.time.DateTime

data class SurveyData(
    val time: DateTime,
    val question: String,
    val answer: String,
    val questionId: Int,
    val option: String? = null
)
