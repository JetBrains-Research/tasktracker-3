package org.jetbrains.research.tasktracker.handler

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.BaseConfig

interface BaseHandler {
    val config: BaseConfig

    fun setup(project: Project) = run { }

    fun destroy() = run { }
}
