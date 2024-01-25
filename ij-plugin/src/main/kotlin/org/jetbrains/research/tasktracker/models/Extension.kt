package org.jetbrains.research.tasktracker.models

enum class Extension(val ext: String) {
    PYTHON(".py"),
    JUPYTER(".ipynb"),
    JAVA(".java"),
    KOTLIN(".kt"),
    CPP(".cpp"),
    CSV(".csv"),
    NO_EXTENSION("")
}
