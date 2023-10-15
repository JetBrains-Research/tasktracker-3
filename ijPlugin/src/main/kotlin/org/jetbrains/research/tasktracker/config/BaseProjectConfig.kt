package org.jetbrains.research.tasktracker.config

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.handler.BaseProjectHandler

interface BaseProjectConfig : BaseConfig {
    fun buildHandler(project: Project): BaseProjectHandler?
}
