@file:Suppress("MaximumLineLength", "MaxLineLength")

package org.jetbrains.research.tasktracker.tracking.survey

import com.intellij.openapi.project.Project
import org.jetbrains.concurrency.await
import org.jetbrains.research.tasktracker.tracking.logger.SurveyLogger
import org.jetbrains.research.tasktracker.ui.main.panel.MainPluginWindow

// TODO: move somewhere and add a config + generate a survey by config
class SurveyParser(private val mainWindow: MainPluginWindow, project: Project) {
    private val surveyLogger = SurveyLogger(project)

   private val items = listOf(
        SurveyItem(
            "hours",
            InputType.Simple,
            "Approximately how many hours did you use the plugin?"
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "On a scale of 1 (very unlikely) to 5 (very likely), how likely are you to continue using the Emotion Tracker in the future?",
            listOf("likely1", "likely2", "likely3", "likely4", "likely5").map { SurveyItem(it, InputType.Radio) }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "On a scale of 1 (not at all) to 5 (extremely), how much did the Emotion Tracker disrupt your workflow?",
            listOf("disrupt1", "disrupt2", "disrupt3", "disrupt4", "disrupt5").map { SurveyItem(it, InputType.Radio) }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "On a scale of 1 (very inaccurate) to 5 (very accurate), how accurately did the Emotion Tracker capture your emotions?",
            listOf("accuracy1", "accuracy2", "accuracy3", "accuracy4", "accuracy5").map {
                SurveyItem(
                    it,
                    InputType.Radio
                )
            }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "On a scale of 1 (not at all) to 5 (very comfortable), how comfortable were you with the plugin recording your emotions while coding?",
            listOf("comfort1", "comfort2", "comfort3", "comfort4", "comfort5").map { SurveyItem(it, InputType.Radio) }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "On a scale of 1 (not at all) to 5 (extremely), how helpful did you find the messages provided by the Emotion Tracker in aiding your self-regulation?",
            listOf("helpful1", "helpful2", "helpful3", "helpful4", "helpful5").map { SurveyItem(it, InputType.Radio) }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "During your experience with the Emotion Tracker, how often did you feel the following emotions? Happiness",
            listOf("never_happy", "rarely_happy", "sometimes_happy", "often_happy", "most_happy").map {
                SurveyItem(
                    it,
                    InputType.Radio
                )
            }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "During your experience with the Emotion Tracker, how often did you feel the following emotions? Surprise",
            listOf(
                "never_surprise",
                "rarely_surprise",
                "sometimes_surprise",
                "often_surprise",
                "most_surprise"
            ).map { SurveyItem(it, InputType.Radio) }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "During your experience with the Emotion Tracker, how often did you feel the following emotions? Neutral",
            listOf(
                "never_neutral",
                "rarely_neutral",
                "sometimes_neutral",
                "often_neutral",
                "most_neutral"
            ).map { SurveyItem(it, InputType.Radio) }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "During your experience with the Emotion Tracker, how often did you feel the following emotions? Sadness",
            listOf("never_sad", "rarely_sad", "sometimes_sad", "often_sad", "most_sad").map {
                SurveyItem(
                    it,
                    InputType.Radio
                )
            }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "During your experience with the Emotion Tracker, how often did you feel the following emotions? Anger",
            listOf("never_anger", "rarely_anger", "sometimes_anger", "often_anger", "most_anger").map {
                SurveyItem(
                    it,
                    InputType.Radio
                )
            }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "During your experience with the Emotion Tracker, how often did you feel the following emotions? Disgust",
            listOf(
                "never_disgust",
                "rarely_disgust",
                "sometimes_disgust",
                "often_disgust",
                "most_disgust"
            ).map { SurveyItem(it, InputType.Radio) }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "During your experience with the Emotion Tracker, how often did you feel the following emotions? Fear",
            listOf("never_fear", "rarely_fear", "sometimes_fear", "often_fear", "most_fear").map {
                SurveyItem(
                    it,
                    InputType.Radio
                )
            }
        ),
        SurveyItem(
            "",
            InputType.Radio,
            "During your experience with the Emotion Tracker, how often did you feel the following emotions? Contempt",
            listOf(
                "never_contempt",
                "rarely_contempt",
                "sometimes_contempt",
                "often_contempt",
                "most_contempt"
            ).map { SurveyItem(it, InputType.Radio) }
        ),
        SurveyItem(
            "additionalThoughts",
            InputType.TextArea,
            "Please share any additional thoughts or comments you have about your experience using the Emotion Tracker plugin."
        ),
    )

    suspend fun parseAndLog() = items.forEachIndexed { index, surveyItem -> parseAndLog(surveyItem, index) }

     private suspend fun parseAndLog(item: SurveyItem, id: Int) {
         when (item.inputType) {
             InputType.Simple, InputType.TextArea -> {
                 val result = mainWindow.getElementValue(item.elementId).await()
                 surveyLogger.log(item.question, result.toString(), questionId = id)
             }
             InputType.Radio -> item.subtypes?.forEach { radioItem ->
                 val result = mainWindow.checkIfRadioButtonChecked(radioItem.elementId).await()
                 surveyLogger.log(item.question, result.toString(), option = radioItem.elementId, questionId = id)
             }
         }
     }
}
