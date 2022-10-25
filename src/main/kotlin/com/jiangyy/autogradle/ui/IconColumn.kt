package com.jiangyy.autogradle.ui

import com.intellij.ui.table.JBTable
import com.jiangyy.autogradle.utils.Icons
import java.awt.Component
import javax.swing.AbstractCellEditor
import javax.swing.JTable
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

class IconColumn(table: JBTable) : AbstractCellEditor(), TableCellEditor,
    TableCellRenderer {

    private var mOperatePanel: JIconPanel = JIconPanel(Icons.GithubIcon)

    init {
        val tableColumn = table.columnModel.getColumn(4)
        tableColumn.cellEditor = this
        tableColumn.cellRenderer = this
        tableColumn.maxWidth = 50
        tableColumn.minWidth = 50
    }

    override fun getTableCellEditorComponent(table: JTable, value: Any, isSelected: Boolean, row: Int, column: Int): Component {
        when (table.model.getValueAt(row, column)) {
            0 -> mOperatePanel.icon = Icons.GithubIcon
            1 -> mOperatePanel.icon = Icons.AndroidIcon
            2 -> mOperatePanel.icon = Icons.GiteeIcon
            3 -> mOperatePanel.icon = Icons.GitlabIcon
            else -> mOperatePanel.icon = Icons.OtherIcon
        }
        return mOperatePanel
    }

    override fun getCellEditorValue(): Any = ""

    override fun getTableCellRendererComponent(
        table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int
    ): Component {
        when (table.model.getValueAt(row, column)) {
            0 -> mOperatePanel.icon = Icons.GithubIcon
            1 -> mOperatePanel.icon = Icons.AndroidIcon
            2 -> mOperatePanel.icon = Icons.GiteeIcon
            3 -> mOperatePanel.icon = Icons.GitlabIcon
            else -> mOperatePanel.icon = Icons.OtherIcon
        }
        return mOperatePanel
    }

}