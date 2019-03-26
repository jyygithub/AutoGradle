package com.jiangyy.action;

import com.alibaba.fastjson.JSON;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.jiangyy.entity.Repository;
import com.jiangyy.ui.HomeDialog;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AutoGradleAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        String path = getClass().getClassLoader().getResource("alldata.json").getFile();
        showNotification(e, "path", "path", path);
        try {
            String input = FileUtils.readFileToString(new File(path), "UTF-8");
            showNotification(e, "input", "input", input);
            List<Repository> data = JSON.parseArray(input, Repository.class);
            List<Repository> data0 = JSON.parseArray(input, Repository.class);

            if (data == null || data0 == null) {
                showNotification(e, "error", "提示", "项目列表解析失败");
            } else {
                HomeDialog dialog = new HomeDialog(e, data, data0);
                dialog.pack();
                dialog.setVisible(true);
            }
        } catch (IOException e1) {
            showNotification(e, "error", "提示", e1.getMessage());
            e1.printStackTrace();
        }
    }

    private String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
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
