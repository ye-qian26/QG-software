package com.qg.domain;


public class ForeignKeyInformation {

    private String foreignKeyTable;
    private String foreignKeyColumn;


    public ForeignKeyInformation() {
    }

    public ForeignKeyInformation(String foreignKeyTable, String foreignKeyColumn) {
        this.foreignKeyTable = foreignKeyTable;
        this.foreignKeyColumn = foreignKeyColumn;
    }

    /**
     * 获取
     * @return foreignKeyTable
     */
    public String getForeignKeyTable() {
        return foreignKeyTable;
    }

    /**
     * 设置
     * @param foreignKeyTable
     */
    public void setForeignKeyTable(String foreignKeyTable) {
        this.foreignKeyTable = foreignKeyTable;
    }

    /**
     * 获取
     * @return foreignKeyColumn
     */
    public String getForeignKeyColumn() {
        return foreignKeyColumn;
    }

    /**
     * 设置
     * @param foreignKeyColumn
     */
    public void setForeignKeyColumn(String foreignKeyColumn) {
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public String toString() {
        return "ForeignKey{foreignKeyTable = " + foreignKeyTable + ", foreignKeyColumn = " + foreignKeyColumn + "}";
    }
}
