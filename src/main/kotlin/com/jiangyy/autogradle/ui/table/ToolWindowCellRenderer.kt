package com.jiangyy.autogradle.ui.table

import com.intellij.ui.JBColor
import com.intellij.ui.dsl.builder.panel
import com.jiangyy.autogradle.entity.Repository
import com.jiangyy.autogradle.utils.orZero
import java.awt.Component
import javax.swing.JList
import javax.swing.ListCellRenderer
import javax.swing.border.EmptyBorder

class ToolWindowCellRenderer : ListCellRenderer<Repository> {

    val mavenTypes = listOf("mavenCentral()", "maven { url 'https://jitpack.io' }", "jCenter()", "google()")

    override fun getListCellRendererComponent(list: JList<out Repository>?, value: Repository?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        return panel {
            row {
                label(value?.name.orEmpty())
            }
            row {
                comment(mavenTypes[value?.mavenType.orZero()])
            }
            row {
                label("""implementation '${value?.groupId}:${value?.artifactId}:${value?.version}'""")
            }
        }.apply {
            background = JBColor.decode("#2d2f31")
            border = EmptyBorder(5, 5, 5, 5)
        }
    }

}