package org.jetbrains.research.tasktracker.config.ide.inspection

import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseProjectConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import org.jetbrains.research.tasktracker.handler.ide.InspectionHandler
import java.io.File

@Serializable
data class InspectionConfig(
    val mode: InspectionMode,
    /**
     * list of inspection short names. List will be applied
     * in accordance with the selected mode.
     */
    val inspectionNames: List<String> = emptyList()
) : BaseProjectConfig {

    override val configName: String
        get() = "inspection"

    override fun buildHandler(project: Project) = InspectionHandler(this, project)

    companion object {
        const val CONFIG_FILE_PREFIX: String = "inspection"

        fun buildConfig(configFile: File): InspectionConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
