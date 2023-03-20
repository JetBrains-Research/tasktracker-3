package org.jetBrains.research.tasktracker.handler.tracking

import org.jetBrains.research.tasktracker.handler.BaseHandler
import org.jetBrains.research.tasktracker.handler.storage.BaseStorageHandler
import kotlin.reflect.KClass

interface BaseTrackingHandler : BaseHandler {
    override val dependentHandlers: List<KClass<*>>
        get() = listOf(BaseStorageHandler::class)
    fun start()
}
