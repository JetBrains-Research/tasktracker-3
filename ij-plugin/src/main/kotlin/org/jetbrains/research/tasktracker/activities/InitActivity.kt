package org.jetbrains.research.tasktracker.activities

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig

// Put into plugin.xml
class InitActivity : ProjectActivity {
    private val logger: Logger = Logger.getInstance(javaClass)

    override suspend fun execute(project: Project) {
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: startup activity")
        TaskTrackerPlugin.initPlugin()
    }
}
