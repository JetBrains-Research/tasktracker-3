package org.jetBrains.research.tasktracker.config.ide

import org.jetBrains.research.tasktracker.config.BaseConfig
import java.io.File

class MainIdeConfig : BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "ide"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: File): MainIdeConfig {
            TODO("Not implemented yet")
        }
    }
}
