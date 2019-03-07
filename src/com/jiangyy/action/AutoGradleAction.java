package com.jiangyy.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jiangyy.ui.HomeDialog;

public class AutoGradleAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        HomeDialog dialog = new HomeDialog(e);
        dialog.pack();
        dialog.setVisible(true);
    }
}
