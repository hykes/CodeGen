package me.hehaiyang.plugins.codegen.model;


import me.hehaiyang.plugins.codegen.utils.BuilderUtil;

import java.io.Serializable;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 16/4/21
 */
public class Field implements Serializable{

    private static final long serialVersionUID = -7928412682947631640L;

    public Field(){}

    public Field(String fieldName, String fieldType, String comment){
        this.field = fieldName;
        this.fieldType = fieldType;
        this.comment = comment;
        this.column = BuilderUtil.camelToUnderline(fieldName);
    }

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
     * 数据库备注
     */
    private String columnType;

    /**
     * 备注
     */
    private String comment;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
