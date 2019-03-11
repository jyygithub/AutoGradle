package com.jiangyy.ui;

import javax.swing.table.AbstractTableModel;

import static com.jiangyy.entity.Config.ALL_DATA;

public class MyTableModel extends AbstractTableModel {

    private String[] columnNames = {"", "Repository", "Version", "Address", "Remark"};

    @Override
    public int getRowCount() {
        return ALL_DATA.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0 || columnIndex == 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object o;
        switch (columnIndex) {
            case 0: {
                o = ALL_DATA[rowIndex].isChoose();
                break;
            }
            case 1: {
                o = ALL_DATA[rowIndex].getName();
                break;
            }
            case 2: {
                o = ALL_DATA[rowIndex].getVersion();
                break;
            }
            case 3: {
                o = "Github";
                break;
            }
            case 4: {
                o = ALL_DATA[rowIndex].getRemark();
                break;
            }
            default: {
                o = "";
                break;
            }
        }
        return o;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                ALL_DATA[rowIndex].setChoose((boolean) aValue);
                break;
            case 2:
                ALL_DATA[rowIndex].setVersion((String) aValue);
                break;
            case 4:
                ALL_DATA[rowIndex].setRemark((String) aValue);
                break;
            default:
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void selectAllOrNull(boolean value) {
        for (int index = 0; index < getRowCount(); index++) {
            this.setValueAt(value, index, 0);
        }
    }

}