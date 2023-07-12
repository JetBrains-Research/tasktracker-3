package org.jetbrains.research.tasktracker.handler

import org.jetbrains.research.tasktracker.config.BaseConfig

interface BaseHandler {
    val config: BaseConfig

    fun setup() = run { }

    fun destroy() = run { }
}
