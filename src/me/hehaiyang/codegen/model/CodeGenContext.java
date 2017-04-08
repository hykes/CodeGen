package me.hehaiyang.codegen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 17/3/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeGenContext implements Serializable {

    private static final long serialVersionUID = -8139936029537532112L;

    public CodeGenContext(String modelName){
        this.modelName = modelName;
    }

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 实体类名称
     */
    private String modelName;

    /**
     * 字段列表
     */
    private List<Field> fields;

}
