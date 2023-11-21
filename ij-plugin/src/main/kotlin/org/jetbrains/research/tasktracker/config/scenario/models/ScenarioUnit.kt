package org.jetbrains.research.tasktracker.config.scenario.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.ide.MainIdeConfig

@Serializable
sealed interface ScenarioUnit

@Serializable
@SerialName("List")
class TaskListUnit(val taskIds: List<String>) : ScenarioUnit

@Serializable
@SerialName("SingleList")
class TaskListWithSingleChoiceUnit(val taskIds: List<String>) : ScenarioUnit

@Serializable
@SerialName("Task")
class TaskUnit(val id: String) : ScenarioUnit

@Serializable
@SerialName("Setting")
class IdeSettingUnit(val mainIdeConfig: MainIdeConfig) : ScenarioUnit

@Serializable
@SerialName("Survey")
class SurveyUnit(val id: String) : ScenarioUnit

@Serializable
@SerialName("External")
class ExternalSourceUnit(val url: String) : ScenarioUnit
