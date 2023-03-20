package org.jetBrains.research.tasktracker.handler

interface BaseHandler {
    fun preAction() = run { }

    fun postAction() = run { }
}
