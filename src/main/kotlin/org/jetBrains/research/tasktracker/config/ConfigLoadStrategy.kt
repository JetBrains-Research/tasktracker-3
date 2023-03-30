package org.jetBrains.research.tasktracker.config

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer

interface ConfigLoadStrategy {
    fun <T> load(text: String, serializer: KSerializer<T>): T
    fun <T> dump(config: T, serializer: KSerializer<T>): String
}

object YamlConfigLoadStrategy : ConfigLoadStrategy {
    override fun <T> load(text: String, serializer: KSerializer<T>): T =
        Yaml.default.decodeFromString(serializer, text)

    override fun <T> dump(config: T, serializer: KSerializer<T>): String =
        Yaml.default.encodeToString(serializer, config)
}
