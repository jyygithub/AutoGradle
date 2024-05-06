package com.jiangyy.autogradle.utils

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object Icons {

    @JvmField
    val GithubIcon: Icon = IconLoader.getIcon("/icons/github.svg", Icons.javaClass)

    @JvmField
    val AndroidIcon: Icon = IconLoader.getIcon("/icons/android.svg", Icons.javaClass)

    @JvmField
    val GiteeIcon: Icon = IconLoader.getIcon("/icons/gitee.svg", Icons.javaClass)

    @JvmField
    val GitlabIcon: Icon = IconLoader.getIcon("/icons/gitlab.svg", Icons.javaClass)

    @JvmField
    val OtherIcon: Icon = IconLoader.getIcon("/icons/other.svg", Icons.javaClass)

}