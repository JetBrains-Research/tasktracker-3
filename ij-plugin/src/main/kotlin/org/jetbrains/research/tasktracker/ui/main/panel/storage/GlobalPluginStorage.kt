package org.jetbrains.research.tasktracker.ui.main.panel.storage

import org.jetbrains.research.tasktracker.ui.main.panel.models.AgreementChecker

object GlobalPluginStorage {

    var agreementChecker: AgreementChecker? = null
    var userId: Int? = null
    var currentResearchId: Int? = null

    fun resetSession() {
        userId = null
        currentResearchId = null
        agreementChecker = null
    }
}
