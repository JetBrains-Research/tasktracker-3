package org.jetBrains.research.tasktracker.properties

enum class TestMode(val propValue: String) {
    ON("on"), OFF("off");

    companion object {
        fun convertToTestMode(testMode: String?) = when (testMode) {
            ON.propValue -> ON
            OFF.propValue -> OFF
            else -> error("Unknown value $testMode in base plugin properties")
        }
    }
}
