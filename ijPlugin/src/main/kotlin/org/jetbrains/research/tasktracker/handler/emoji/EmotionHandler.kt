package org.jetbrains.research.tasktracker.handler.emoji

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.emoji.EmotionConfig
import org.jetbrains.research.tasktracker.handler.BaseHandler

class EmotionHandler(override val config: EmotionConfig) : BaseHandler {

    override fun setup(project: Project) {
//        TODO("Setup emojis according to the config")
    }
}
