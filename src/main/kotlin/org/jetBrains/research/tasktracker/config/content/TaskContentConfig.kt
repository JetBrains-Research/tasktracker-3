package org.jetBrains.research.tasktracker.config.content

import org.jetBrains.research.tasktracker.config.BaseConfig
import java.io.File

class TaskContentConfig : BaseConfig {
    companion object {
        const val CONFIG_FILE_PREFIX: String = "task_content"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: File): TaskContentConfig {
            TODO("Not implemented yet")
        }
    }
}
