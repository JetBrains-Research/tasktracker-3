package org.jetbrains.research.tasktracker.tracking.webcam

import kotlinx.serialization.Serializable

@Serializable
data class WebCamInfo(val deviceNumber: Int, val pictureExample: String)
