package me.hehaiyang.codegen.model;


import lombok.Data;
import me.hehaiyang.codegen.utils.BuilderUtil;

import java.io.Serializable;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 16/4/21
 */
@Data
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
    
}
