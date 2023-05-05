package org.jetBrains.research.tasktracker.tracking

import org.jetBrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME
import org.jetBrains.research.tasktracker.models.Extension
import java.util.*

object DefaultContentProvider {
    fun getDefaultFolderRelativePath(task: Task) =
        "$PLUGIN_NAME/${task.getExtension().name.lowercase(Locale.getDefault())}"

    fun getDefaultContent(task: Task) = when (task.getExtension()) {
        Extension.JAVA ->
            getPackage(task.getExtension()) +
                "public class Solution {\n" +
                "    public static void main(String[] args) {\n" +
                "        // Write your code here\n" +
                "    }" +
                "\n}"

        Extension.KOTLIN ->
            getPackage(task.getExtension()) +
                "fun main() {\n" +
                "    // Write your code here\n" +
                "}"

        Extension.CPP ->
            "#include <iostream>\n" +
                "\n" +
                "int main() \n" +
                "{ \n" +
                "    // Write your code here\n" +
                "    return 0; \n" +
                "}"

        Extension.PYTHON -> "# Write your code here"
        else -> ""
    }

    private fun getPackage(extension: Extension): String {
        val currentPackage =
            "package $PLUGIN_NAME.${extension.name.lowercase(Locale.getDefault())}"
        return when (extension) {
            Extension.JAVA -> "$currentPackage;\n\n"
            Extension.KOTLIN -> "$currentPackage\n\n"
            else -> ""
        }
    }
}
