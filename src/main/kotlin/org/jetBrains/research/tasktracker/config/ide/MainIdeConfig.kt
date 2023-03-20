package org.jetBrains.research.tasktracker.config.ide

import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.handler.BaseHandler
import java.io.File

// TODO: make data class
class MainIdeConfig : BaseConfig {
    override val handler: BaseHandler = TODO("Not implemented yet")

    companion object {
        const val CONFIG_FILE_PREFIX: String = "ide"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: File): MainIdeConfig {
            TODO("Not implemented yet")
        }
    }
}
