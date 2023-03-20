package org.jetBrains.research.tasktracker

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetBrains.research.tasktracker.handler.tracking.BaseTrackingHandler

// Put into plugin.xml
class InitActivity : StartupActivity {
    private val logger: Logger = Logger.getInstance(javaClass)

    init {
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: startup activity")
    }

    override fun runActivity(project: Project) {
        TaskTrackerPlugin.mainConfig.configs.forEach { it.handler.preAction() }
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: run tracking")
        TaskTrackerPlugin.mainConfig.configs.forEach { (it.handler as? BaseTrackingHandler)?.start() }
        TaskTrackerPlugin.mainConfig.configs.forEach { it.handler.postAction() }
    }
}
