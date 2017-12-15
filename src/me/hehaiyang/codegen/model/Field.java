package me.hehaiyang.codegen.model;


import me.hehaiyang.codegen.parser.ParserUtils;
import me.hehaiyang.codegen.utils.BuilderUtil;

import java.io.Serializable;
import java.sql.Types;

/**
 * Desc: 字段
 * Mail: hehaiyangwork@qq.com
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
    private String kotlinType;

    /**
     * 数据库字段名
     */
    private String column;

    /**
     * 数据库字段类型
     */
    private String columnType;

    /**
     * 数据库字段类型
     * @see java.sql.Types
     */
    private Integer sqlType;

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
    public void setKotlinType(String kotlinType) {
        this.kotlinType = kotlinType;
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
        // 默认sqlType为NULL类型
       this.setColumnType(columnType, sqlType == null ? Types.NULL : sqlType);
    }
    public void setColumnType(String columnType, Integer sqlType) {
        this.columnType = columnType;
        this.sqlType = sqlType;

        FieldType fieldType = ParserUtils.getFieldType(columnType);
        if (fieldType.getJavaType().equals("UNKNOWN")) {
            fieldType = ParserUtils.getFieldType(sqlType);
        }
        this.fieldType = fieldType.getJavaType();
        this.kotlinType = fieldType.getKotlinType();
    }


    public String getKotlinType() {
        return kotlinType;
    }

    public Integer getSqlType() {
        return sqlType;
    }

    public void setSqlType(Integer sqlType) {
        this.sqlType = sqlType;
    }
    public void setSqlType(String sqlType) {
        try {
            this.sqlType = Integer.parseInt(sqlType);
        } catch (Exception e) { // ignore error
            this.sqlType = Types.NULL;
        }
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
