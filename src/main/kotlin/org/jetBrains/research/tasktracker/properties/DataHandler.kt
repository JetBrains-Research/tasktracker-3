package org.jetbrains.research.tasktracker.properties

enum class DataHandler(val propValue: String) {
    LOCAL_FILE("local_file"), SERVER_CONNECTION("server_connection");

    companion object {
        fun convertToDataHandler(dataHandler: String?) = when (dataHandler) {
            LOCAL_FILE.propValue -> LOCAL_FILE
            SERVER_CONNECTION.propValue -> SERVER_CONNECTION
            else -> error("Unknown value $dataHandler in base plugin properties")
        }
    }
}
