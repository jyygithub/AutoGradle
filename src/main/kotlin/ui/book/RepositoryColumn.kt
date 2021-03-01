package ui.book

import com.intellij.ui.table.JBTable
import entity.Repository
import java.awt.Component
import javax.swing.AbstractCellEditor
import javax.swing.JTable
import javax.swing.table.TableCellRenderer

class RepositoryColumn(table: JBTable, column: Int) : AbstractCellEditor(), TableCellRenderer {

    private val mOperatePanel: JRepositoryPanel = JRepositoryPanel()

    override fun getCellEditorValue(): Any = ""

    override fun getTableCellRendererComponent(
        table: JTable,
        value: Any,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        mOperatePanel.setRepository(value as Repository)
        return mOperatePanel
    }

    init {
        val tableColumn = table.columnModel.getColumn(column)
        tableColumn.cellRenderer = this
    }

}