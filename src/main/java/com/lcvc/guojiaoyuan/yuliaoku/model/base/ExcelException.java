package com.lcvc.guojiaoyuan.yuliaoku.model.base;

/**
 * 用于记录表格上传中出现的错误信息
 *
 */
public class ExcelException {
    private String sheetName;//sheet工作表名
    private Integer row;//表格的行
    private Integer column;//表格的列
    private String value;//该单元格的值
    private String errorMessage;//错误的信息

    public ExcelException() {
    }


    public ExcelException(String sheetName, Integer row, Integer column, String value, String errorMessage) {
        this.sheetName = sheetName;
        this.row = row;
        this.column = column;
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
