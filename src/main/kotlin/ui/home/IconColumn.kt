package ui.home

import com.intellij.ui.table.JBTable
import icons.Icons
import java.awt.Component
import javax.swing.AbstractCellEditor
import javax.swing.JTable
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

class IconColumn(table: JBTable, column: Int) : AbstractCellEditor(), TableCellEditor,
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
            "github" -> mOperatePanel.icon = Icons.GithubIcon
            "android" -> mOperatePanel.icon = Icons.AndroidIcon
            else -> mOperatePanel.icon = Icons.GithubIcon
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
            "github" -> mOperatePanel.icon = Icons.GithubIcon
            "android" -> mOperatePanel.icon = Icons.AndroidIcon
            else -> mOperatePanel.icon = Icons.GithubIcon
        }
        return mOperatePanel
    }

    init {
        val tableColumn = table.columnModel.getColumn(column)
        tableColumn.cellEditor = this
        tableColumn.cellRenderer = this
        tableColumn.maxWidth = 50
        tableColumn.minWidth = 50
    }

}