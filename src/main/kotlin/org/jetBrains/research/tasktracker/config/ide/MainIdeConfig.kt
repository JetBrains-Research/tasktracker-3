package org.jetBrains.research.tasktracker.config.ide

import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.handler.BaseHandler
import java.nio.file.Path

// TODO: make data class
class MainIdeConfig : BaseConfig {
    override val handler: BaseHandler = TODO("Not yet implemented")

    companion object {
        const val CONFIG_FILE_PREFIX: String = "ide"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: Path): MainIdeConfig {
            TODO("Not yet implemented")
        }
    }
}
