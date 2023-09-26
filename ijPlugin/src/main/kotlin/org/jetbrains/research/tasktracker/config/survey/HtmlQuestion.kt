package org.jetbrains.research.tasktracker.config.survey

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RadioInfo(val id: String, val value: String)

@Serializable
sealed class HtmlQuestion {
    abstract val text: String
    abstract val elementId: String
    private val textSize: Int = defaultTextSize
        get() = if (field < minTextSize || field > maxTextSize) defaultTextSize else field

    @SerialName("required")
    private val isRequired: Boolean = false
    abstract fun toHtml(): String

    protected fun Any?.asParameter(name: String): String = this?.let { "$name=\"$it\"" } ?: ""
    protected fun isRequiredString(): String = if (isRequired) "required" else ""
    protected fun htmlText() = "<h$textSize>$text</h$textSize>"

    companion object {
        private const val minTextSize = 1
        private const val maxTextSize = 5
        private const val defaultTextSize = 3
    }
}

@Serializable
@SerialName("Input")
data class InputHtmlQuestion(
    override val text: String,
    override val elementId: String,
    private val type: String = defaultType, // TODO enum?
    private val min: Int? = null,
    private val max: Int? = null,
    private val step: Float? = null
) :
    HtmlQuestion() {
    override fun toHtml(): String = buildString {
        append(htmlText())
        append("<input type=\"$type\" id=\"$elementId\" name=\"$elementId\" ")
        append("${min.asParameter("min")} ${max.asParameter("max")} ")
        append("${step.asParameter("step")} ${isRequiredString()}><br>")
    }

    companion object {
        private const val defaultType = "text"
    }
}

@Serializable
@SerialName("Radio")
data class RadioHtmlQuestion(
    override val text: String,
    override val elementId: String,
    @SerialName("info") val infos: List<RadioInfo>
) : HtmlQuestion() {
    override fun toHtml(): String = buildString {
        append(htmlText())
        infos.forEach { (id, value) ->
            append("<input type=\"radio\" id=\"$id\" ${elementId.asParameter("name")} ")
            append("value=\"$value\" ${isRequiredString()}>")
            append("<label for=\"$id\">$value</label><br>")
        }
    }
}

@Serializable
@SerialName("Textarea")
data class TextAreaHtmlQuestion(
    override val text: String,
    override val elementId: String,
    val rows: Int? = null,
    val cols: Int? = null
) : HtmlQuestion() {
    override fun toHtml(): String = buildString {
        append(htmlText())
        append("<textarea name=\"$elementId\" id=\"$elementId\" ${rows.asParameter("rows")} ")
        append("${cols.asParameter("cols")} ${isRequiredString()}></textarea>")
    }
}

@Serializable
@SerialName("Container")
data class HtmlQuestionContainer(override val text: String) : HtmlQuestion() {
    override val elementId: String
        get() = ""

    @SerialName("questions")
    val subQuestions: List<HtmlQuestion> = emptyList()

    override fun toHtml(): String = buildString {
        append(htmlText())
        subQuestions.forEach {
            append(it.toHtml())
        }
    }
}
