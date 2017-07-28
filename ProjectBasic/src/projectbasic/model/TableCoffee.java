/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.model;

/**
 *
 * @author Visual Studio
 */
public class TableCoffee {

    private String idTable;
    private String nameTable;
    private boolean isState = false;
    private TableData tableData;

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public boolean isIsState() {
        return isState;
    }

    public void setIsState(boolean isState) {
        this.isState = isState;
    }

    public TableData getTableData() {
        return tableData;
    }

    public void setTableData(TableData tableData) {
        this.tableData = tableData;
    }
    
    public TableCoffee(String idTable, String nameTable, TableData tableData) {
        this.idTable = idTable;
        this.nameTable = nameTable;
        this.tableData = tableData;
    }
    
}
