package org.jetbrains.research.tasktracker.handler.ide

import com.intellij.codeInspection.ex.InspectionProfileImpl
import com.intellij.codeInspection.ex.InspectionToolRegistrar
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager
import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionConfig
import org.jetbrains.research.tasktracker.config.ide.inspection.InspectionMode
import org.jetbrains.research.tasktracker.handler.BaseHandler

class InspectionHandler(override val config: InspectionConfig) : BaseHandler {
    private var inspectionDisposable: Disposable? = null
    private val logger: Logger = Logger.getInstance(InspectionHandler::class.java)

    override fun setup(project: Project) {
        // creating a new profile to make changes only in the current project
        val profile = initTaskProfile(project)
        applyConfig(profile, project)
    }

    fun applyConfig(profile: InspectionProfileImpl, project: Project) {
        inspectionDisposable = inspectionDisposable ?: Disposer.newDisposable()
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
                    config.inspectionNames.disableInspections(profile, project)
                }

                InspectionMode.ADD_SELECTED -> config.inspectionNames.enableInspections(profile, project)
            }
        }
    }

    private fun initTaskProfile(project: Project): InspectionProfileImpl {
        val inspectionProfileManager = ProjectInspectionProfileManager.getInstance(project)
        val inspectionProfile = InspectionProfileImpl(
            PROFILE_NAME,
            InspectionToolRegistrar.getInstance(),
            inspectionProfileManager
        )
        inspectionProfile.copyFrom(inspectionProfileManager.currentProfile)
        inspectionProfile.isProjectLevel = true
        inspectionProfile.name = PROFILE_NAME
        inspectionProfileManager.addProfile(inspectionProfile)
        inspectionProfileManager.setCurrentProfile(inspectionProfile)
        return inspectionProfileManager.currentProfile
    }

    override fun destroy() {
        inspectionDisposable?.let { Disposer.dispose(it) }
    }

    private fun Collection<String>.enableInspections(profile: InspectionProfileImpl, project: Project) =
        filterExistingInspections(profile).forEach {
            profile.enableTool(it, project)
        }

    private fun Collection<String>.disableInspections(profile: InspectionProfileImpl, project: Project) =
        filterExistingInspections(profile).forEach {
            profile.setToolEnabled(it, false, project)
        }

    private fun Collection<String>.filterExistingInspections(profile: InspectionProfileImpl): List<String> =
        partition { inspection -> inspection in profile.allTools.map { it.tool.shortName } }.apply {
            second.forEach {
                logger.warn("There are no inspection with short name '$it'")
            }
        }.first

    companion object {
        private const val PROFILE_NAME = "task"
    }
}
