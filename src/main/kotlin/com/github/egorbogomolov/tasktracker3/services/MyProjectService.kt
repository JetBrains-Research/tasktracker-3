package com.github.egorbogomolov.tasktracker3.services

import com.github.egorbogomolov.tasktracker3.MyBundle
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
        thisLogger().warn(
            "Don't forget to remove all non-needed sample code files with" +
                " their corresponding registration entries in `plugin.xml`."
        )
    }

    @Suppress("MagicNumber")
    fun getRandomNumber() = (1..100).random()
}
