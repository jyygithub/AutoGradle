package com.jiangyy.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jiangyy.ui.HomeDialog;
import com.jiangyy.utils.ParserUtils;
import org.jetbrains.annotations.NotNull;

public class AutoGradleAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        HomeDialog dialog = new HomeDialog(e);
        dialog.pack();
        dialog.setVisible(true);

        String path = getClass().getClassLoader().getResource("alldata.json").getPath();
        String s = ParserUtils.readJsonFile(path);


    }

}
