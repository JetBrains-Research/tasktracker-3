package org.jetbrains.research.tasktracker.requests

import com.intellij.openapi.diagnostic.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.research.tasktracker.TaskTrackerPlugin
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.getRoute
import org.jetbrains.research.tasktracker.ui.main.panel.storage.GlobalPluginStorage

object IdRequests {
    private val client = HttpClient(CIO)
    private val logger = Logger.getInstance(IdRequests::class.java)

    @Suppress("TooGenericExceptionCaught")
    fun getUserId(name: String, email: String): Int? =
        runBlocking {
            val url = getRoute("create-user")
            try {
                return@runBlocking client.submitForm(
                    url = url,
                    formParameters = parameters {
                        append("name", name)
                        append("email", email)
                    }
                ).body<Int>()
            } catch (e: Exception) {
                logger.warn("Server interaction error while getting user id! Url: $url", e)
            }
            return@runBlocking null
        }

    @Suppress("TooGenericExceptionCaught")
    fun getResearchId(): Int? =
        runBlocking {
            val url = getRoute("create-research")
            val pluginInfoConfig = TaskTrackerPlugin.mainConfig.pluginInfoConfig
                ?: error("MainPageConfig must not be null")
            try {
                requireNotNull(GlobalPluginStorage.userId) { "User id is not defined" }
                return@runBlocking client.submitForm(
                    url = url,
                    formParameters = parameters {
                        append("name", pluginInfoConfig.pluginName)
                        append("description", pluginInfoConfig.pluginDescription)
                        append("user_id", GlobalPluginStorage.userId.toString())
                    }
                ).body<Int>()
            } catch (e: IllegalArgumentException) {
                logger.warn(e.localizedMessage)
            } catch (e: Exception) {
                logger.warn("Server interaction error while getting user id! Url: $url", e)
            }
            return@runBlocking null
        }
}
