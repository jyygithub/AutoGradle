package com.jiangyy.autogradle.utils

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

inline fun Int?.orZero(): Int = this ?: 0
inline fun Boolean?.orDefault(): Boolean = this ?: false

object Icons {

    @JvmField
    val GithubIcon: Icon = IconLoader.getIcon("/META-INF/github.svg", Icons.javaClass)

    @JvmField
    val AndroidIcon: Icon = IconLoader.getIcon("/META-INF/android.svg", Icons.javaClass)

    @JvmField
    val GiteeIcon: Icon = IconLoader.getIcon("/META-INF/gitee.svg", Icons.javaClass)

    @JvmField
    val GitlabIcon: Icon = IconLoader.getIcon("/META-INF/gitlab.svg", Icons.javaClass)

    @JvmField
    val OtherIcon: Icon = IconLoader.getIcon("/META-INF/other.svg", Icons.javaClass)

}