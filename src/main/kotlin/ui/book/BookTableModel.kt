package ui.book

import entity.Repository
import javax.swing.table.AbstractTableModel

class BookTableModel(private val repositories: MutableList<Repository>) : AbstractTableModel() {

    private val mColumnNames = mutableListOf("Repository")

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
        return repositories[rowIndex]
    }

    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
        repositories[rowIndex] = value as Repository
        fireTableCellUpdated(rowIndex, columnIndex)
    }

}