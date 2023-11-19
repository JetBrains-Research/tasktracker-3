package org.jetbrains.research.tasktracker.config.scenario.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig
import org.jetbrains.research.tasktracker.config.survey.SurveyConfig

@Serializable
sealed interface ScenarioUnit

@Serializable
@SerialName("List")
class TaskListUnit(val tasks: List<Int>) : ScenarioUnit

@Serializable
@SerialName("SingleList")
class TaskListWithSingleChoiceUnit(val tasks: List<Int>) : ScenarioUnit

@Serializable
@SerialName("Task")
class TaskUnit(val id: String) : ScenarioUnit

@Serializable
@SerialName("Setting")
class IdeSettingUnit(val mainIdeConfig: MainIdeConfig) : ScenarioUnit

@Serializable
@SerialName("Survey")
class SurveyUnit(val surveyConfig: SurveyConfig) : ScenarioUnit

@Serializable
@SerialName("External")
class ExternalSourceUnit(val url: String) : ScenarioUnit
