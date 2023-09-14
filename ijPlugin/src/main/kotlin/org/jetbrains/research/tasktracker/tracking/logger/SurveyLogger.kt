package org.jetbrains.research.tasktracker.tracking.logger

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.tracking.survey.SurveyData
import org.joda.time.DateTime

class SurveyLogger(val project: Project) : BaseLogger() {
    // TODO: to list of printers
    override val logPrinter: LogPrinter = initLogPrinter("survey_${project.hashCode()}_${project.name}")
        .also {
            it.csvPrinter.printRecord(SurveyLoggedData.headers)
        }

    fun log(
        question: String,
        answer: String,
        questionId: Int,
        option: String? = null
    ) = log(SurveyLoggedData.getData(SurveyData(DateTime.now(), question, answer, questionId, option = option)))
}
