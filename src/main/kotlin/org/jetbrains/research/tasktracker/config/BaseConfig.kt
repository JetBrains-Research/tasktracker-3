package org.jetbrains.research.tasktracker.config

import org.jetbrains.research.tasktracker.handler.BaseHandler

interface BaseConfig {
    fun buildHandler(): BaseHandler? = null
}
