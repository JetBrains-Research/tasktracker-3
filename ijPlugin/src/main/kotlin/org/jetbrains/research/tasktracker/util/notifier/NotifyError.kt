package org.jetbrains.research.tasktracker.util.notifier

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nls
import org.jetbrains.research.tasktracker.config.MainTaskTrackerConfig.Companion.PLUGIN_NAME

fun notifyError(project: Project, content: @Nls String?) {
    NotificationGroupManager.getInstance().getNotificationGroup(PLUGIN_NAME)
        .createNotification(content ?: "Unknown error", NotificationType.ERROR).notify(project)
}
