package org.jetbrains.research.tasktracker.handler.ide

import com.intellij.codeInspection.ex.InspectionProfileImpl
import com.intellij.codeInspection.ex.InspectionToolRegistrar
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.profile.codeInspection.InspectionProfileManager
import com.intellij.testFramework.disableInspections
import com.intellij.testFramework.enableInspectionTool
import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionConfig
import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionMode
import org.jetbrains.research.tasktracker.handler.BaseHandler

class InspectionHandler(override val config: InspectionConfig) : BaseHandler {
    private var inspectionDisposable: Disposable? = null
    private val logger: Logger = Logger.getInstance(InspectionHandler::class.java)

    override fun setup(project: Project) {
        inspectionDisposable = Disposer.newDisposable()
        // creating a new profile to make changes only in the current project
        val inspectionProfileManager = InspectionProfileManager.getInstance(project)
        InspectionProfileImpl(
            PROFILE_NAME,
            InspectionToolRegistrar.getInstance(),
            inspectionProfileManager.currentProfile
        )
        inspectionProfileManager.setRootProfile(PROFILE_NAME)

        val profile = inspectionProfileManager.currentProfile
        with(profile) {
            when (config.mode) {
                InspectionMode.ALL -> enableAllTools(project)
                InspectionMode.NONE -> disableAllTools(project)
                InspectionMode.DEFAULT -> {}
                InspectionMode.ENABLE_SELECTED -> {
                    disableAllTools(project)
                    config.inspectionNames.enableInspections(profile, project)
                }

                InspectionMode.DISABLE_SELECTED -> {
                    val tools = config.inspectionNames.toInspectionTools(profile, project).map { it.tool }
                    disableInspections(project, inspections = tools.toTypedArray())
                }

                InspectionMode.ADD_SELECTED -> config.inspectionNames.enableInspections(profile, project)
            }
        }
    }

    override fun destroy() {
        inspectionDisposable?.let { Disposer.dispose(it) }
    }

    private fun Collection<String>.enableInspections(profile: InspectionProfileImpl, project: Project) =
        toInspectionTools(profile, project).forEach {
            enableInspectionTool(
                project,
                it,
                inspectionDisposable ?: error("disposable must be initiated for this moment")
            )
        }

    private fun Collection<String>.toInspectionTools(profile: InspectionProfileImpl, project: Project) =
        mapNotNull {
            profile.getInspectionTool(it, project)
                .also { inspection ->
                    inspection ?: logger.warn("There are no inspection with short name '$it'")
                }
        }

    companion object {
        private const val PROFILE_NAME = "task"
    }
}
