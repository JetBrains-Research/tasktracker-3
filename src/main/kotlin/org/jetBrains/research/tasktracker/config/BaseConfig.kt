package org.jetBrains.research.tasktracker.config

import org.jetBrains.research.tasktracker.handler.BaseHandler
import kotlin.reflect.KClass

interface BaseConfig {
    // Indicates if we need to run all actions from the current config after configs from the list
    val dependentConfigs: List<KClass<*>>
        get() = emptyList()

    val handler: BaseHandler
}
