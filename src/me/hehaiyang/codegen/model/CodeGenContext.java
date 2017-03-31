package me.hehaiyang.codegen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 17/3/21
 */
@Data
public class CodeGenContext implements Serializable {

    private static final long serialVersionUID = -8139936029537532112L;

    public CodeGenContext(){}

    public CodeGenContext(String modelName){
        this.modelName = modelName;
    }

    public CodeGenContext(String modelName, String tableName, List<Field> fields){
        this.tableName = tableName;
        this.fields = fields;
        this.modelName = modelName;
    }

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 字段列表
     */
    private List<Field> fields;

    /**
     * 实体类名称
     */
    private String modelName;

}
