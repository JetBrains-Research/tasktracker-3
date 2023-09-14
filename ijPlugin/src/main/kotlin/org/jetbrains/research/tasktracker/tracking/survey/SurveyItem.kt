package org.jetbrains.research.tasktracker.tracking.survey

enum class InputType {
    Simple,
    Radio,
    TextArea
}

// TODO: decomposition
data class SurveyItem(
    val elementId: String,
    val inputType: InputType,
    val question: String = "",
    val subtypes: List<SurveyItem>? = null
)
