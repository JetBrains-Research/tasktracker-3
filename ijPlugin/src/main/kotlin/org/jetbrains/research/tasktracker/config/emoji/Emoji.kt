package org.jetbrains.research.tasktracker.config.emoji

import com.intellij.openapi.util.IconLoader
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import javax.swing.Icon

@Serializable
data class Emoji(
    val modelPosition: Int,
    val name: String,
    private val affirmDescriptions: List<String>,
    private val adviceDescriptions: List<String> = emptyList()
) {
    private val iconName: String? = null
    private val modalWindowIconName: String? = null

    @Transient
    val icon: Icon? = iconName?.let { IconLoader.getIcon(it, Emoji::class.java) }

    @Transient
    val modalWindowIcon: Icon? = modalWindowIconName?.let { IconLoader.getIcon(it, Emoji::class.java) }

    val randomAffirmDescription
        get() = affirmDescriptions.random()

    val randomAdviceDescription
        get() = adviceDescriptions.random()
}