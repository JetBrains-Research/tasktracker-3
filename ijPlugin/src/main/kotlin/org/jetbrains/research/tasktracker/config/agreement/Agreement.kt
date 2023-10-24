package org.jetbrains.research.tasktracker.config.agreement

import kotlinx.serialization.Serializable

@Serializable
data class Agreement(val text: String, val required: Boolean = true)
