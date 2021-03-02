package ui.book

import entity.Repository
import javax.swing.table.AbstractTableModel

class BookTableModel(private val repositories: MutableList<Repository>) : AbstractTableModel() {

    private val mColumnNames = mutableListOf("Repository", "")

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

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return when (columnIndex) {
            0 -> repositories[rowIndex]
            1 -> repositories[rowIndex].warehouse.orEmpty()
            else -> Unit
        }
    }

    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
        when (columnIndex) {
            0 -> repositories[rowIndex] = value as Repository
            1 -> repositories[rowIndex].warehouse = value as String?
            else -> Unit
        }

        fireTableCellUpdated(rowIndex, columnIndex)
    }

}