package org.jetBrains.research.tasktracker.activities

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetBrains.research.tasktracker.TaskTrackerPlugin
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetBrains.research.tasktracker.handler.init.InitializationHandler
import org.jetBrains.research.tasktracker.properties.DataHandler

// Put into plugin.xml
class InitActivity : StartupActivity {
    private val logger: Logger = Logger.getInstance(javaClass)

    init {
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: startup activity")
    }

    override fun runActivity(project: Project) {
        when (TaskTrackerPlugin.mainConfig.pluginProperties.dataHandler) {
            DataHandler.LOCAL_FILE -> InitializationHandler(TaskTrackerPlugin.mainConfig).setupEnvironment()
            DataHandler.SERVER_CONNECTION -> {
                TODO("Check if we need to show extra content ot we can just setupEnvironment")
            }
        }
    }
}
