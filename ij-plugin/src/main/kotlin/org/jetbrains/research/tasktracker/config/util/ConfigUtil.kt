package org.jetbrains.research.tasktracker.config.util

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.research.tasktracker.config.BaseConfig
import java.io.File

inline fun <reified T : BaseConfig> buildBaseConfig(
    config: T?,
    configFile: File,
    configBuilder: (File) -> T,
    logger: Logger
): T {
    require(config == null) {
        "The ${config?.configName} config was already parsed"
    }
    return configBuilder(configFile).also {
        logger.info("Building ${it.configName} config...")
    }
}
