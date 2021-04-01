package com.github.hykes.codegen.utils;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/20
 */
public class MyNotifier {

    public static void notifyError(@Nullable Project project, String content) {
        NotificationGroupManager.getInstance().getNotificationGroup("CodeGen.Notification.Group")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

}
