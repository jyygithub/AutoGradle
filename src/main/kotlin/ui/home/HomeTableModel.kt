package ui.home

import entity.Repository
import javax.swing.table.AbstractTableModel

class HomeTableModel(private val repositories: MutableList<Repository>) : AbstractTableModel() {

    private val mColumnNames = mutableListOf("", "Repository", "Current Version", "Custom Version", "")

    override fun getRowCount(): Int {
        return repositories.size
    }

    override fun getColumnCount(): Int {
        return mColumnNames.size
    }

    override fun getColumnName(columnIndex: Int): String {
        return mColumnNames[columnIndex]
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return getValueAt(0, columnIndex).javaClass
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return columnIndex == 0 || columnIndex == 3
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return when (columnIndex) {
            0 -> repositories[rowIndex].isChoose
            1 -> repositories[rowIndex].nickname
            2 -> repositories[rowIndex].version
            3 -> repositories[rowIndex].customVersion.orEmpty()
            4 -> repositories[rowIndex].warehouse.orEmpty()
            else -> ""
        }
    }

    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
        when (columnIndex) {
            0 -> repositories[rowIndex].isChoose = value as Boolean
            3 -> repositories[rowIndex].customVersion = value as String?
            4 -> repositories[rowIndex].warehouse = value as String?
        }
        fireTableCellUpdated(rowIndex, columnIndex)
    }

}