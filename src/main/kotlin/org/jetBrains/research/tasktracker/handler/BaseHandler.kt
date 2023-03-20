package org.jetBrains.research.tasktracker.handler

import kotlin.reflect.KClass

interface BaseHandler {
    // Indicates if we need to run all actions from the current handler after handlers from the list
    val dependentHandlers: List<KClass<*>>
        get() = emptyList()

    fun preAction() = run { }

    fun postAction() = run { }
}
