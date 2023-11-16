package org.jetbrains.research.tasktracker.config.scenario.models

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.survey.SurveyConfig

@Serializable
sealed interface ScenarioUnit

@Serializable
class TaskListUnit(val tasks: List<Int>) : ScenarioUnit

@Serializable
class TaskUnit(val id: Int) : ScenarioUnit

@Serializable
class IdeSettingUnit(val mainIdeConfig: MainIdeConfig) : ScenarioUnit

@Serializable
class SurveyUnit(val surveyConfig: SurveyConfig) : ScenarioUnit

@Serializable
class ExternalSourceUnit(val url: String) : ScenarioUnit
