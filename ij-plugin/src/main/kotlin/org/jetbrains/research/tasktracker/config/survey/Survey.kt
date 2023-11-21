package org.jetbrains.research.tasktracker.config.survey

import kotlinx.serialization.Serializable

@Serializable
data class Survey(val id: String, val htmlQuestions: List<HtmlQuestion>) {
    fun toHtml() = htmlQuestions.joinToString(System.lineSeparator()) { it.toHtml() }
}
