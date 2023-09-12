package org.jetbrains.research.tasktracker.config

import org.jetbrains.research.tasktracker.handler.BaseHandler

interface BaseConfig {
    val configName: String

    fun buildHandler(): BaseHandler? = null
}
