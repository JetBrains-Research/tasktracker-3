package org.jetbrains.research.tasktracker.config.survey

import kotlinx.serialization.Serializable

enum class InputType {
    Simple,
    Radio,
    TextArea
}

@Serializable
data class SurveyItem(
    val elementId: String,
    val inputType: InputType,
    val question: String = "",
    val subtypes: List<SurveyItem>? = null
)
