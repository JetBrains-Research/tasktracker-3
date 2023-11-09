package org.jetbrains.research.tasktracker.config.agreement

import kotlinx.serialization.Serializable
import org.jetbrains.research.tasktracker.config.BaseConfig
import org.jetbrains.research.tasktracker.config.YamlConfigLoadStrategy
import java.io.File

@Serializable
class AgreementConfig(val agreements: List<Agreement>) : BaseConfig {
    override val configName: String
        get() = "agreement"

    companion object {
        const val CONFIG_FILE_PREFIX: String = "agreement"

        fun buildConfig(configFile: File): AgreementConfig =
            YamlConfigLoadStrategy.load(configFile.readText(), serializer())
    }
}
