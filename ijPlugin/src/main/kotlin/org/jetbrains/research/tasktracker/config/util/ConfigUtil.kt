package org.jetbrains.research.tasktracker.config.util

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.BaseConfig
import java.io.File

inline fun <reified T : BaseConfig> buildBaseConfig(
    configFile: File,
    configBuilder: (File) -> T,
    logger: Logger
): T {
    val config = configBuilder(configFile)
    logger.info("config ${config.configName} has been built...")
    return config
}
