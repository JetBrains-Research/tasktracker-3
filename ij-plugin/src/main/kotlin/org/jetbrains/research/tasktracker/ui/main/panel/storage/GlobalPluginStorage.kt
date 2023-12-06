package org.jetbrains.research.tasktracker.ui.main.panel.storage

import org.jetbrains.research.tasktracker.modelInference.EmoPredictor
import org.jetbrains.research.tasktracker.tracking.webcam.WebCamInfo
import org.jetbrains.research.tasktracker.ui.main.panel.models.AgreementChecker

object GlobalPluginStorage {
    val camerasInfo: MutableList<WebCamInfo> = mutableListOf()
    var currentDeviceNumber: Int? = null

    var emoPredictor: EmoPredictor? = null

    var agreementChecker: AgreementChecker? = null
    var userId: Int? = null
    var currentResearchId: Int? = null
}
