package org.jetbrains.research.tasktracker.actions.tracking

import com.intellij.openapi.project.Project
import org.jetbrains.research.tasktracker.actions.BaseWrapper
import javax.swing.Icon

class NotificationWrapper(
    override val icon: Icon,
    override val text: String,
    override val project: Project? = null
) : BaseWrapper(project) {
    init {
        isResizable = false
        // TODO: move to the base class
        title = "It’s feedback o’clock!"
        cancelAction.isEnabled = false
        init()
    }

    companion object {
        // TODO: move to configs
        const val FEEDBACK_TEXT = """
            <p>Thank you for using our emotion-tracking plugin!</p>
            <p>We appreciate your feedback as it helps us improve.</p>
            <p>Would you like to take a brief survey now to share your thoughts and experiences?</p><p></p>

            <p>If you'd rather complete the survey later, you can access it anytime by clicking the "TaskTracker"</p>
            <p>panel in the upper right corner of your IDE.</p>
        """
    }
}
