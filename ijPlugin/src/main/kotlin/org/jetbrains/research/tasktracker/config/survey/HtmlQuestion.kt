package org.jetbrains.research.tasktracker.config.survey

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Radio button id and value
 */
@Serializable
data class RadioInfo(val id: String, val value: String)

/**
 * This is a class that constructs an input field of a specified type in HTML.
 */
@Serializable
sealed class HtmlQuestion {
    /**
     * `h` tag text
     */
    abstract val text: String
    abstract val elementId: String

    /**
     * `h` tag size. For example, h1, h2, and so on.
     */
    private val textSize: Int = defaultTextSize
        get() = if (field < minTextSize || field > maxTextSize) defaultTextSize else field

    /**
     * Sets 'required' on the input if this field is mandatory to fill out.
     */
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
    /**
     * input type. text, number, etc.
     */
    private val type: String = defaultType,
    /**
     * min parameter in the input.
     */
    private val min: Int? = null,
    /**
     * max parameter in the input.
     */
    private val max: Int? = null,
    /**
     * step parameter in the input.
     */
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
    /**
     * **ElementId** here is radio button **name**!!!.
     */
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
    /**
     * rows parameter in the input.
     */
    val rows: Int? = null,
    /**
     * cols parameter in the input.
     */
    val cols: Int? = null
) : HtmlQuestion() {
    override fun toHtml(): String = buildString {
        append(htmlText())
        append("<textarea name=\"$elementId\" id=\"$elementId\" ${rows.asParameter("rows")} ")
        append("${cols.asParameter("cols")} ${isRequiredString()}></textarea>")
    }
}

/**
 * Represents container in which tou can store sub questions with general title.
 */
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
