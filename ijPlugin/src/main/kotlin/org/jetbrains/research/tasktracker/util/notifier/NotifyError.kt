package org.jetbrains.research.tasktracker.util.notifier

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls

fun notifyError(project: Project, content: @Nls String?) {
    println("hello")
    NotificationGroupManager.getInstance().getNotificationGroup("Tasktracker")
        .createNotification(content ?: "Unknown error", NotificationType.ERROR).notify(project)
}
