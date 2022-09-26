package ui.table

import com.intellij.ui.table.JBTable
import com.jiangyy.autogradle.ui.JIconPanel
import com.jiangyy.autogradle.utils.Icons
import java.awt.Component
import javax.swing.AbstractCellEditor
import javax.swing.JTable
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

class IconColumn(table: JBTable) : AbstractCellEditor(), TableCellEditor,
        TableCellRenderer {

    private val mOperatePanel: JIconPanel = JIconPanel(Icons.GithubIcon)

    override fun getTableCellEditorComponent(
            table: JTable,
            value: Any,
            isSelected: Boolean,
            row: Int,
            column: Int
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

    override fun getCellEditorValue(): Any = ""

    override fun getTableCellRendererComponent(
            table: JTable,
            value: Any,
            isSelected: Boolean,
            hasFocus: Boolean,
            row: Int,
            column: Int
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

    init {
        val tableColumn = table.columnModel.getColumn(4)
        tableColumn.cellEditor = this
        tableColumn.cellRenderer = this
        tableColumn.maxWidth = 50
        tableColumn.minWidth = 50
    }

}