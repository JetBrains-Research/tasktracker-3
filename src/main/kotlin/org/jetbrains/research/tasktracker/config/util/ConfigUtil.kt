package org.jetbrains.research.tasktracker.config.util

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.BaseConfig
import java.io.File

object ConfigUtil {
    inline fun <reified T : BaseConfig> buildConfig(
        config: T?,
        configName: String,
        configFile: File,
        configBuilder: (File) -> T
    ): T {
        require(config == null) {
            "The $configName config was already parsed"
        }
        val logger = Logger.getInstance(T::class.java)
        logger.info("Building $configName config...")
        return configBuilder(configFile)
    }
}
