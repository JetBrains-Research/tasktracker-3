package org.jetbrains.research.tasktracker.config.agreement

import kotlinx.serialization.Serializable

/**
 * text can contain urls, which should be covered in <a> tag.
 */

@Serializable
data class Agreement(val text: String, val required: Boolean = true, val openLinkInDefaultBrowser: Boolean = true)
