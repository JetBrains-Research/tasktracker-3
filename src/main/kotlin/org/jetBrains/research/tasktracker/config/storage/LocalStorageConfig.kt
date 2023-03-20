package org.jetBrains.research.tasktracker.config.storage

import org.jetBrains.research.tasktracker.config.BaseConfig
import org.jetBrains.research.tasktracker.handler.BaseHandler
import java.nio.file.Path

data class LocalStorageConfig(
    val trackingFolder: Path
) : BaseConfig, BaseStorageConfig {
    override val handler: BaseHandler = TODO("Not yet implemented")
    override fun initStorage() {
        TODO("Not yet implemented")
    }

    companion object {
        const val CONFIG_FILE_PREFIX: String = "local_storage"

        @Suppress("UnusedPrivateMember")
        fun buildConfig(configFile: Path): LocalStorageConfig {
            TODO("Not yet implemented")
        }
    }
}
