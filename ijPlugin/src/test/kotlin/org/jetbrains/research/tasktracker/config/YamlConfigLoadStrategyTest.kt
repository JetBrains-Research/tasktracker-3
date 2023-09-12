package org.jetbrains.research.tasktracker.config

import org.jetbrains.research.tasktracker.config.tracking.CodeTrackingConfig
import org.junit.Assert.assertEquals
import org.junit.Test

class YamlConfigLoadStrategyTest {
    private fun loadConfig() = YamlConfigLoadStrategyTest::class.java
        .getResource("codeTrackingConfig.yaml")?.readText()
        ?: error("Cannot find codeTrackingConfig.yaml!")

    @Test
    fun loadCodeTrackingConfig() {
        assertEquals(CodeTrackingConfig(), YamlConfigLoadStrategy.load(loadConfig(), CodeTrackingConfig.serializer()))
    }

    @Test
    fun dumpCodeTrackingConfig() {
        assertEquals(loadConfig(), YamlConfigLoadStrategy.dump(CodeTrackingConfig(), CodeTrackingConfig.serializer()))
    }
}
