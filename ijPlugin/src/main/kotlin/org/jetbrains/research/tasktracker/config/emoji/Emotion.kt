package org.jetbrains.research.tasktracker.config.emoji

import com.intellij.openapi.util.IconLoader
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import javax.swing.Icon

@Serializable
open class Emotion(
    val modelPosition: Int,
    val name: String,
    private val affirmDescriptions: List<String>,
    private val adviceDescriptions: List<String> = emptyList()
) {
    protected open val iconName: String? = null
    protected open val modalWindowIconName: String? = null

    @Transient
    val icon: Icon? = iconName?.let { IconLoader.getIcon(it, Emotion::class.java) }

    @Transient
    val modalWindowIcon: Icon? = modalWindowIconName?.let { IconLoader.getIcon(it, Emotion::class.java) }

    val randomAffirmDescription
        get() = affirmDescriptions.random()

    val randomAdviceDescription
        get() = adviceDescriptions.random()
}
