package me.hehaiyang.codegen.model;


import me.hehaiyang.codegen.utils.BuilderUtil;
import me.hehaiyang.codegen.windows.DBOperation;

import java.io.Serializable;

/**
 * Desc: 字段
 * Mail: hehaiyang@terminus.io
 * Date: 16/4/21
 */
public class Field implements Serializable{

    private static final long serialVersionUID = -7928412682947631640L;

    /**
     * 属性名
     */
    private String field;

    /**
     * 属性类型
     */
    private String fieldType;

    /**
     * 数据库字段名
     */
    private String column;

    /**
     * 数据库字段类型
     */
    private String columnType;

    /**
     * 数据库字段长度
     */
    private String columnSize;

    /**
     * 备注
     */
    private String comment;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
        this.column = BuilderUtil.camelToUnderline(this.field);
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
        this.field = BuilderUtil.underlineToCamel(this.column);
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {

        this.columnType = columnType;
        this.fieldType = DBOperation.toJavaType(columnType);
    }

    public String getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(String columnSize) {
        this.columnSize = columnSize;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
