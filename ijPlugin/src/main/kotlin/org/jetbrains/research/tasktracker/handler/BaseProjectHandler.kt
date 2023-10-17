package org.jetbrains.research.tasktracker.handler

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.config.BaseConfig

interface BaseProjectHandler : BaseHandler {
    override val config: BaseConfig

    val project: Project
}
