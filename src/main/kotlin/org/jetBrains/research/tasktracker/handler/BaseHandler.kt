package org.jetBrains.research.tasktracker.handler

import org.jetBrains.research.tasktracker.config.BaseConfig

interface BaseHandler {
    val config: BaseConfig

    fun setup() = run { }

    fun destroy() = run { }
}
