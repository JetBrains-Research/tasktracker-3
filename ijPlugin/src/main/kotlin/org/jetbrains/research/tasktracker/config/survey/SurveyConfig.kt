package org.jetbrains.research.tasktracker.config.survey

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
class SurveyConfig : BaseConfig {
    val surveyItems: List<SurveyItem> = emptyList()
    override val configName: String
        get() = CONFIG_FILE_PREFIX

    companion object {
        const val CONFIG_FILE_PREFIX: String = "survey"

        fun buildConfig(configFile: File): SurveyConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
