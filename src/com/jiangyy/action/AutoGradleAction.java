package com.jiangyy.action;

import com.alibaba.fastjson.JSON;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jiangyy.entity.Repository;
import com.jiangyy.ui.HomeDialog;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.jiangyy.entity.DataKt.JSON_STR;

public class AutoGradleAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        List<Repository> data = JSON.parseArray(JSON_STR, Repository.class);
        List<Repository> data0 = JSON.parseArray(JSON_STR, Repository.class);

        if (data == null || data0 == null) {
            showNotification(e, "error", "提示", "项目列表解析失败");
        } else {
            HomeDialog dialog = new HomeDialog(e, data, data0);
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private void showNotification(@NotNull AnActionEvent e, String displayId, String title, String message) {
        NotificationGroup noti = new NotificationGroup(displayId, NotificationDisplayType.BALLOON, true);
        noti.createNotification(
                title,
                message,
                NotificationType.INFORMATION,
                null
        ).notify(e.getProject());
    }

}
