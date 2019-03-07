package com.jiangyy.ui;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public final class MyIcons {

    private MyIcons() {
        throw new AssertionError("icons.MyIcons"
                + " instances for you!");
    }

    public static final Icon TOOLBAR_LOGO = IconLoader.getIcon("/icon/toolbar_logo.png");

}
