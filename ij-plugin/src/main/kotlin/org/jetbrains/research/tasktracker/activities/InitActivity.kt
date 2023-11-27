package org.jetbrains.research.tasktracker.activities

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig

// Put into plugin.xml
class InitActivity : StartupActivity {
    private val logger: Logger = Logger.getInstance(javaClass)

    init {
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: startup activity")
        TaskTrackerPlugin.initPlugin()
    }

    // TODO: show an error message to the user if an error occurs
    override fun runActivity(project: Project) {
        TaskTrackerPlugin.initializationHandler.setupEnvironment(project) // TODO: move to the start point of the task
    }
}
