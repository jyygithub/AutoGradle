package com.jiangyy.autogradle.ui.table

import com.intellij.ui.JBColor
import com.intellij.ui.dsl.builder.panel
import com.jiangyy.autogradle.entity.Repository
import com.jiangyy.autogradle.utils.orZero
import java.awt.Component
import javax.swing.JList
import javax.swing.ListCellRenderer

class ToolWindowCellRenderer : ListCellRenderer<Repository> {

    val mavenTypes = listOf("mavenCentral()", "maven { url 'https://jitpack.io' }", "jCenter()", "google()")
    val utlTypes = listOf("github", "android", "gitee", "gitlab", "url")

    override fun getListCellRendererComponent(list: JList<out Repository>?, value: Repository?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        return panel {
            group(value?.nickname.orEmpty()) {
                row {
                    browserLink(utlTypes[value?.urlType.orZero()], value?.url.orEmpty())
                    comment(mavenTypes[value?.mavenType.orZero()])
                }
                row {
                    label("""implementation '${value?.groupId}:${value?.artifactId}:${value?.version}'""").apply {
                        component.background = JBColor.YELLOW
                    }
                }
            }
        }
    }

}