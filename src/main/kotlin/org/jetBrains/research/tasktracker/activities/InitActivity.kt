package org.jetBrains.research.tasktracker.activities

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetBrains.research.tasktracker.TaskTrackerPlugin
import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig
import org.jetBrains.research.tasktracker.handler.scenario.ScenarioHandler

// Put into plugin.xml
class InitActivity : StartupActivity {
    private val logger: Logger = Logger.getInstance(javaClass)

    init {
        logger.info("${MainTaskTrackerConfig.PLUGIN_NAME}: startup activity")
        TaskTrackerPlugin.initPlugin()
    }

    // TODO: show an error message to the user if an error occurs
    override fun runActivity(project: Project) {
        TaskTrackerPlugin.mainConfig.scenarioConfig?.let { scenarioConf ->
            scenarioConf.scenario.getNextStep()?.let { scenarioStep ->
                with(ScenarioHandler(TaskTrackerPlugin.mainConfig)) {
                    scenarioStep.run()
                }
            } ?: logger.error("Try to init the plugin, but the scenario is empty or invalid")
        } ?: logger.error("Please, provide a scenario for the plugin behaviour")
    }
}
