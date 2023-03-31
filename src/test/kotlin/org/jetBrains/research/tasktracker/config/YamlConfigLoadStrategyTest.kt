package org.jetBrains.research.tasktracker.config

import org.jetBrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.junit.Test
import kotlin.test.assertEquals

class YamlConfigLoadStrategyTest {
    @Test
    fun loadCodeTrackingConfig() {
        val config = javaClass.classLoader.getResource("config/codeTrackingConfig.yaml")?.readText() ?: ""
        assertEquals(CodeTrackingConfig(), YamlConfigLoadStrategy.load(config, CodeTrackingConfig.serializer()))
    }

    @Test
    fun dumpCodeTrackingConfig() {
        val config = javaClass.classLoader.getResource("config/codeTrackingConfig.yaml")?.readText() ?: ""
        assertEquals(config, YamlConfigLoadStrategy.dump(CodeTrackingConfig(), CodeTrackingConfig.serializer()))
    }
}
